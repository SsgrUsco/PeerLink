package co.edu.usco.peerlink.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Modelo de entrada para el registro publico de estudiantes y tutores.
 */
@Data
@Schema(description = "Registro publico de estudiantes y tutores.")
public class UsuarioRegistroDTO {

    @NotBlank(message = "{validation.usuario.nombre.notBlank}")
    @Schema(example = "Ana Gomez")
    private String nombreCompleto;

    @NotBlank(message = "{validation.usuario.correo.notBlank}")
    @Email(message = "{validation.usuario.correo.email}")
    @Schema(example = "ana@peerlink.edu.co")
    private String correo;

    @NotBlank(message = "{validation.usuario.password.notBlank}")
    @Size(min = 10, max = 72, message = "{validation.usuario.password.size}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "{validation.usuario.password.pattern}")
    @Schema(example = "Seguro1234", writeOnly = true)
    private String password;

    @NotBlank(message = "{validation.usuario.rol.notBlank}")
    @Pattern(regexp = "ESTUDIANTE|TUTOR", message = "{validation.usuario.rol.pattern}")
    @Schema(example = "ESTUDIANTE", allowableValues = {"ESTUDIANTE", "TUTOR"})
    private String rol;
}
