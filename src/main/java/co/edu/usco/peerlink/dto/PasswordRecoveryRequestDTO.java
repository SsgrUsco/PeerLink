package co.edu.usco.peerlink.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Solicitud de ayuda para recuperar acceso sin revelar si el correo existe.
 */
@Data
@Schema(description = "Solicitud de recuperacion asistida de acceso. No confirma si el correo existe ni cambia contrasenas automaticamente.")
public class PasswordRecoveryRequestDTO {
    @NotBlank(message = "{validation.usuario.correo.notBlank}")
    @Email(message = "{validation.usuario.correo.email}")
    @Schema(description = "Correo institucional o personal que el usuario desea recuperar.", example = "estudiante@correo.com")
    private String correo;
}
