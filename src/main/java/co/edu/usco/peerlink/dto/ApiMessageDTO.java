package co.edu.usco.peerlink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Representa una respuesta textual simple para operaciones sin cuerpo complejo.
 */
@Data
@AllArgsConstructor
@Schema(description = "Respuesta simple de confirmacion.")
public class ApiMessageDTO {
    @Schema(example = "Operacion realizada correctamente.")
    private String message;
}
