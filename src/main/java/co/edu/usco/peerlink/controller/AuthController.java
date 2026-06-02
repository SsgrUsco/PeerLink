package co.edu.usco.peerlink.controller;

import co.edu.usco.peerlink.dto.ApiMessageDTO;
import co.edu.usco.peerlink.dto.AuthDTO;
import co.edu.usco.peerlink.dto.AuthResponseDTO;
import co.edu.usco.peerlink.dto.PasswordRecoveryRequestDTO;
import co.edu.usco.peerlink.exception.BusinessException;
import co.edu.usco.peerlink.security.AuthenticatedUser;
import co.edu.usco.peerlink.security.JwtUtil;
import co.edu.usco.peerlink.security.LoginAttemptService;
import co.edu.usco.peerlink.security.SecurityAuditLogger;
import co.edu.usco.peerlink.service.PasswordRecoverySupportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST responsable de la autenticacion de usuarios.
 *
 * <p>Expone login, logout y recuperacion asistida. El login emite el JWT
 * mediante cookie segura HttpOnly para reducir exposicion del token en
 * JavaScript del navegador.</p>
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticacion", description = "Servicios publicos para login, cookie JWT HttpOnly, cierre de sesion y recuperacion asistida.")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final LoginAttemptService loginAttemptService;
    private final SecurityAuditLogger auditLogger;
    private final PasswordRecoverySupportService passwordRecoverySupportService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          LoginAttemptService loginAttemptService,
                          SecurityAuditLogger auditLogger,
                          PasswordRecoverySupportService passwordRecoverySupportService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.loginAttemptService = loginAttemptService;
        this.auditLogger = auditLogger;
        this.passwordRecoverySupportService = passwordRecoverySupportService;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesion",
            description = "Autentica un usuario con correo y contrasena. Si las credenciales son validas, emite una cookie segura AUTH_TOKEN con el JWT y devuelve los datos basicos del usuario. El rol se obtiene desde usuarios_rol."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticacion exitosa",
                    headers = @Header(name = HttpHeaders.SET_COOKIE, description = "Cookie AUTH_TOKEN con HttpOnly, Secure y SameSite=Lax."),
                    content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
            @ApiResponse(responseCode = "401", description = "Credenciales invalidas"),
            @ApiResponse(responseCode = "429", description = "Demasiados intentos de inicio de sesion")
    })
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthDTO authDTO, HttpServletRequest request) {
        String remoteAddress = request.getRemoteAddr();
        if (loginAttemptService.isBlocked(authDTO.getCorreo(), remoteAddress)) {
            auditLogger.log("LOGIN_BLOCKED", authDTO.getCorreo(), remoteAddress);
            throw new BusinessException("error.auth.tooManyAttempts", HttpStatus.TOO_MANY_REQUESTS);
        }

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authDTO.getCorreo(), authDTO.getPassword())
            );
        } catch (BadCredentialsException exception) {
            loginAttemptService.recordFailure(authDTO.getCorreo(), remoteAddress);
            auditLogger.log("LOGIN_FAILED", authDTO.getCorreo(), remoteAddress);
            throw exception;
        }

        if (!(authentication.getPrincipal() instanceof AuthenticatedUser user)) {
            loginAttemptService.recordFailure(authDTO.getCorreo(), remoteAddress);
            auditLogger.log("LOGIN_FAILED", authDTO.getCorreo(), remoteAddress);
            throw new BadCredentialsException("Credenciales invalidas");
        }

        loginAttemptService.recordSuccess(authDTO.getCorreo(), remoteAddress);
        auditLogger.log("LOGIN_SUCCESS", user.getUsername(), remoteAddress);
        String token = jwtUtil.generateToken(user);
        ResponseCookie authCookie = ResponseCookie.from("AUTH_TOKEN", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/")
                .maxAge(jwtUtil.getExpirationDuration())
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authCookie.toString())
                .body(new AuthResponseDTO(
                        null,
                        "Bearer",
                        user.getRol(),
                        user.getUsername(),
                        user.getNombreCompleto()
                ));
    }

    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesion", description = "Elimina la cookie HttpOnly AUTH_TOKEN usada para autenticar la sesion actual.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sesion cerrada",
                    headers = @Header(name = HttpHeaders.SET_COOKIE, description = "Cookie AUTH_TOKEN expirada."),
                    content = @Content(schema = @Schema(implementation = ApiMessageDTO.class)))
    })
    public ResponseEntity<ApiMessageDTO> logout() {
        auditLogger.log("LOGOUT", "-", "cookie-cleared");
        ResponseCookie expiredCookie = ResponseCookie.from("AUTH_TOKEN", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, expiredCookie.toString())
                .body(new ApiMessageDTO("logout"));
    }

    @PostMapping("/password-recovery/support")
    @Operation(
            summary = "Solicitar recuperacion asistida",
            description = """
                    Registra una solicitud de ayuda para recuperar acceso sin cambiar contrasenas automaticamente.

                    Seguridad del flujo:
                    - La respuesta al usuario es generica para no revelar si el correo existe.
                    - Soporte recibe un correo por SMTP/Mailtrap con el correo ingresado y si existe cuenta asociada.
                    - El ADMIN debe verificar identidad antes de usar el endpoint de restablecimiento temporal.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitud recibida",
                    content = @Content(schema = @Schema(implementation = ApiMessageDTO.class))),
            @ApiResponse(responseCode = "400", description = "Correo invalido")
    })
    public ResponseEntity<ApiMessageDTO> solicitarRecuperacion(@Valid @RequestBody PasswordRecoveryRequestDTO dto) {
        passwordRecoverySupportService.requestAssistedRecovery(dto.getCorreo());
        auditLogger.log("PASSWORD_RECOVERY_REQUESTED", dto.getCorreo(), "assisted");
        return ResponseEntity.ok(new ApiMessageDTO("password-recovery-requested"));
    }
}
