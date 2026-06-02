package co.edu.usco.peerlink.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Datos que el administrador utiliza para crear o gestionar usuarios internos.
 */
@Data
@Schema(description = "Datos usados por ADMIN para crear usuarios internos.")
public class UsuarioGestionDTO {

    @NotBlank(message = "{validation.usuario.nombre.notBlank}")
    @Schema(example = "Laura Perez")
    private String nombreCompleto;

    @NotBlank(message = "{validation.usuario.correo.notBlank}")
    @Email(message = "{validation.usuario.correo.email}")
    @Schema(example = "laura@peerlink.edu.co")
    private String correo;

    @NotBlank(message = "{validation.usuario.password.notBlank}")
    @Size(min = 10, max = 72, message = "{validation.usuario.password.size}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "{validation.usuario.password.pattern}")
    @Schema(example = "Seguro1234", writeOnly = true)
    private String password;

    @NotBlank(message = "{validation.usuario.rol.notBlank}")
    @Pattern(regexp = "ADMIN|ESTUDIANTE|TUTOR", message = "{validation.usuario.rol.adminPattern}")
    @Schema(example = "TUTOR", allowableValues = {"ADMIN", "ESTUDIANTE", "TUTOR"})
    private String rol;
}
