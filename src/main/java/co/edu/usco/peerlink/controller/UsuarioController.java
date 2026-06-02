package co.edu.usco.peerlink.controller;

import co.edu.usco.peerlink.dto.AdminPasswordResetDTO;
import co.edu.usco.peerlink.dto.UsuarioDTO;
import co.edu.usco.peerlink.dto.UsuarioGestionDTO;
import co.edu.usco.peerlink.dto.UsuarioPasswordUpdateDTO;
import co.edu.usco.peerlink.dto.UsuarioPerfilUpdateDTO;
import co.edu.usco.peerlink.dto.UsuarioRegistroDTO;
import co.edu.usco.peerlink.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para operaciones de usuarios.
 *
 * <p>Gestiona registro publico, perfil autenticado, administracion de usuarios
 * y restablecimiento de contrasenas por parte de ADMIN.</p>
 */
@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Gestion de usuarios, perfil, registro publico y restablecimiento administrativo de contrasenas.")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    @Operation(
            summary = "Registrar usuario publico",
            description = """
                    Crea una cuenta nueva con rol ESTUDIANTE o TUTOR. Este endpoint es publico y devuelve los datos basicos del usuario creado.

                    Politica de contrasena:
                    - 10 a 72 caracteres.
                    - Al menos una minuscula, una mayuscula y un numero.
                    - No puede iniciar o terminar con espacios.
                    - No puede ser una contrasena comun ni contener el correo del usuario.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario registrado",
                    content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validacion fallida"),
            @ApiResponse(responseCode = "409", description = "Correo ya registrado")
    })
    public ResponseEntity<UsuarioDTO> registrar(@Valid @RequestBody UsuarioRegistroDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearUsuario(dto));
    }

    @PostMapping
    @Operation(
            summary = "Crear usuario desde administracion",
            description = "Permite a un ADMIN crear usuarios internos con el rol indicado. La informacion se persiste en tablas satelite 6NF de usuarios y la contrasena aplica la politica de seguridad del sistema."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado",
                    content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validacion fallida"),
            @ApiResponse(responseCode = "403", description = "Acceso restringido a ADMIN")
    })
    public ResponseEntity<UsuarioDTO> crear(@Valid @RequestBody UsuarioGestionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearUsuario(dto));
    }

    @GetMapping
    @Operation(
            summary = "Listar usuarios",
            description = "Devuelve el listado completo de usuarios registrados. Solo debe ser usado por el rol ADMIN."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de usuarios",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UsuarioDTO.class)))),
            @ApiResponse(responseCode = "403", description = "Acceso restringido a ADMIN")
    })
    public ResponseEntity<List<UsuarioDTO>> listar() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @GetMapping("/me")
    @Operation(
            summary = "Consultar mi perfil",
            description = "Devuelve los datos del usuario autenticado, sin exponer la contrasena."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil actual",
                    content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
            @ApiResponse(responseCode = "401", description = "Token ausente o invalido")
    })
    public ResponseEntity<UsuarioDTO> miPerfil() {
        return ResponseEntity.ok(usuarioService.obtenerPerfilActual());
    }

    @PutMapping("/me")
    @Operation(
            summary = "Actualizar mi perfil",
            description = "Actualiza nombre completo y correo del usuario autenticado."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil actualizado",
                    content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validacion fallida"),
            @ApiResponse(responseCode = "401", description = "Token ausente o invalido"),
            @ApiResponse(responseCode = "409", description = "Correo ya registrado")
    })
    public ResponseEntity<UsuarioDTO> actualizarMiPerfil(@Valid @RequestBody UsuarioPerfilUpdateDTO dto) {
        return ResponseEntity.ok(usuarioService.actualizarPerfilActual(dto));
    }

    @PatchMapping("/me/password")
    @Operation(
            summary = "Actualizar mi contrasena",
            description = """
                    Cambia la contrasena del usuario autenticado validando la contrasena actual.

                    Politica de contrasena nueva:
                    - 10 a 72 caracteres.
                    - Al menos una minuscula, una mayuscula y un numero.
                    - No puede iniciar o terminar con espacios.
                    - No puede ser una contrasena comun ni contener el correo del usuario.
                    """
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Contrasena actualizada"),
            @ApiResponse(responseCode = "400", description = "Validacion fallida o contrasena actual incorrecta"),
            @ApiResponse(responseCode = "401", description = "Token ausente o invalido")
    })
    public ResponseEntity<Void> actualizarMiPassword(@Valid @RequestBody UsuarioPasswordUpdateDTO dto) {
        usuarioService.actualizarPasswordActual(dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/password")
    @Operation(
            summary = "Restablecer contrasena de usuario",
            description = """
                    Permite a un ADMIN asignar una nueva contrasena temporal a un usuario existente.

                    Este endpoint complementa la recuperacion asistida: el usuario solicita ayuda, soporte verifica identidad y el ADMIN define una contrasena temporal. La contrasena no se envia automaticamente por correo.

                    La nueva contrasena aplica la misma politica de seguridad del registro.
                    """
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Contrasena restablecida"),
            @ApiResponse(responseCode = "400", description = "Validacion fallida"),
            @ApiResponse(responseCode = "403", description = "Acceso restringido a ADMIN"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Void> restablecerPassword(
            @Parameter(description = "ID del usuario.", example = "3")
            @PathVariable Integer id,
            @Valid @RequestBody AdminPasswordResetDTO dto) {
        usuarioService.restablecerPasswordPorAdmin(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina un usuario por su identificador. Operacion reservada para ADMIN."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
            @ApiResponse(responseCode = "403", description = "Acceso restringido a ADMIN"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del usuario a eliminar.", example = "3")
            @PathVariable Integer id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
