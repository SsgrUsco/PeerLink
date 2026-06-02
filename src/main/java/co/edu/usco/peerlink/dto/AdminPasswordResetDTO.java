package co.edu.usco.peerlink.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Transporta la nueva contrasena que un administrador asigna a un usuario.
 */
@Data
@Schema(description = "Restablecimiento de contrasena realizado por un administrador.")
public class AdminPasswordResetDTO {
    @NotBlank(message = "{validation.usuario.password.notBlank}")
    @Size(min = 10, max = 72, message = "{validation.usuario.password.size}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "{validation.usuario.password.pattern}")
    @Schema(
            description = "Nueva contrasena temporal. Debe cumplir la politica de seguridad del sistema.",
            example = "AccesoSeguro123",
            writeOnly = true
    )
    private String passwordNueva;
}
