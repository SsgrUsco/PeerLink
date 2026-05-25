package co.edu.usco.peerlink.controller;

import co.edu.usco.peerlink.dto.UsuarioGestionDTO;
import co.edu.usco.peerlink.dto.UsuarioDTO;
import co.edu.usco.peerlink.dto.UsuarioRegistroDTO;
import co.edu.usco.peerlink.dto.UsuarioPasswordUpdateDTO;
import co.edu.usco.peerlink.dto.UsuarioPerfilUpdateDTO;
import co.edu.usco.peerlink.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios y registro público.")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    @Operation(
            summary = "Registrar usuario público",
            description = "Crea una cuenta nueva con rol ESTUDIANTE o TUTOR. Este endpoint es público y devuelve los datos básicos del usuario creado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario registrado",
                    content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validación fallida"),
            @ApiResponse(responseCode = "409", description = "Correo ya registrado")
    })
    public ResponseEntity<UsuarioDTO> registrar(@Valid @RequestBody UsuarioRegistroDTO dto) {
        return new ResponseEntity<>(usuarioService.crearUsuario(dto), HttpStatus.CREATED);
    }

    @PostMapping
    @Operation(
            summary = "Crear usuario desde administración",
            description = "Permite a un ADMIN crear usuarios internos con el rol indicado."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado",
                    content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validación fallida"),
            @ApiResponse(responseCode = "403", description = "Acceso restringido a ADMIN")
    })
    public ResponseEntity<UsuarioDTO> crear(@Valid @RequestBody UsuarioGestionDTO dto) {
        return new ResponseEntity<>(usuarioService.crearUsuario(dto), HttpStatus.CREATED);
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
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UsuarioDTO> miPerfil() {
        return ResponseEntity.ok(usuarioService.obtenerPerfilActual());
    }

    @PutMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UsuarioDTO> actualizarMiPerfil(@Valid @RequestBody UsuarioPerfilUpdateDTO dto) {
        return ResponseEntity.ok(usuarioService.actualizarPerfilActual(dto));
    }

    @PatchMapping("/me/password")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> actualizarMiPassword(@Valid @RequestBody UsuarioPasswordUpdateDTO dto) {
        usuarioService.actualizarPasswordActual(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina un usuario por su identificador. Operación reservada para ADMIN."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "403", description = "Acceso restringido a ADMIN")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
