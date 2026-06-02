package co.edu.usco.peerlink.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Contiene las credenciales enviadas por el cliente durante el inicio de sesion.
 */
@Data
@Schema(description = "Credenciales de acceso para generar un token JWT.")
public class AuthDTO {

    @NotBlank(message = "{validation.auth.correo.notBlank}")
    @Email(message = "{validation.auth.correo.email}")
    @Schema(example = "admin@peerlink.edu.co")
    private String correo;

    @NotBlank(message = "{validation.auth.password.notBlank}")
    @Schema(description = "Contrasena del usuario. Se envia solo en el cuerpo de login.", example = "AdminSeguro123", writeOnly = true)
    private String password;
}
