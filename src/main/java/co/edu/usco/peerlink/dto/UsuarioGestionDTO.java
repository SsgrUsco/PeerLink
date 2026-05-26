package co.edu.usco.peerlink.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

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
    @Schema(example = "password123", writeOnly = true)
    private String password;

    @NotBlank(message = "{validation.usuario.rol.notBlank}")
    @Pattern(regexp = "ADMIN|ESTUDIANTE|TUTOR", message = "{validation.usuario.rol.adminPattern}")
    @Schema(example = "TUTOR", allowableValues = {"ADMIN", "ESTUDIANTE", "TUTOR"})
    private String rol;
}
