package co.edu.usco.peerlink.controller;

import co.edu.usco.peerlink.dto.AuthDTO;
import co.edu.usco.peerlink.dto.AuthResponseDTO;
import co.edu.usco.peerlink.security.AuthenticatedUser;
import co.edu.usco.peerlink.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticacion", description = "Servicios publicos para autenticacion y emision de JWT.")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesion",
            description = "Autentica un usuario con correo y contrasena. Si las credenciales son validas, devuelve un JWT con vigencia de 24 horas y el rol obtenido desde usuarios_rol."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticacion exitosa",
                    content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
            @ApiResponse(responseCode = "401", description = "Credenciales invalidas")
    })
    public AuthResponseDTO login(@Valid @RequestBody AuthDTO authDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authDTO.getCorreo(), authDTO.getPassword())
        );

        if (!(authentication.getPrincipal() instanceof AuthenticatedUser user)) {
            throw new BadCredentialsException("Credenciales invalidas");
        }

        return new AuthResponseDTO(
                jwtUtil.generateToken(user),
                "Bearer",
                user.getRol(),
                user.getUsername(),
                user.getNombreCompleto()
        );
    }
}
