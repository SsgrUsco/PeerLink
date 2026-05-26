package co.edu.usco.peerlink.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Cambio de contrasena del usuario autenticado.")
public class UsuarioPasswordUpdateDTO {

    @NotBlank
    @Schema(example = "password123", writeOnly = true)
    private String passwordActual;

    @NotBlank
    @Schema(example = "nuevaPassword123", writeOnly = true)
    private String passwordNueva;
}
