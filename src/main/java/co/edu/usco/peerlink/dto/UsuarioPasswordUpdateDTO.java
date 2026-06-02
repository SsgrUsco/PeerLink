package co.edu.usco.peerlink.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Solicitud de cambio de contrasena ejecutada por el usuario autenticado.
 */
@Data
@Schema(description = "Cambio de contrasena del usuario autenticado.")
public class UsuarioPasswordUpdateDTO {

    @NotBlank
    @Schema(description = "Contrasena actual del usuario autenticado.", example = "ActualSegura123", writeOnly = true)
    private String passwordActual;

    @NotBlank
    @Size(min = 10, max = 72, message = "{validation.usuario.password.size}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "{validation.usuario.password.pattern}")
    @Schema(description = "Nueva contrasena segun la politica de seguridad vigente.", example = "NuevaSegura123", writeOnly = true)
    private String passwordNueva;
}
