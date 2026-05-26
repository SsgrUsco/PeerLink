package co.edu.usco.peerlink.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

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
    @Schema(example = "password123", writeOnly = true)
    private String password;

    @NotBlank(message = "{validation.usuario.rol.notBlank}")
    @Pattern(regexp = "ESTUDIANTE|TUTOR", message = "{validation.usuario.rol.pattern}")
    @Schema(example = "ESTUDIANTE", allowableValues = {"ESTUDIANTE", "TUTOR"})
    private String rol;
}
