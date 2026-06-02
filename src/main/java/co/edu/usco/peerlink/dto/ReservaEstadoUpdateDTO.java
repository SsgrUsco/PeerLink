package co.edu.usco.peerlink.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Solicitud para cambiar el estado operativo de una reserva existente.
 */
@Data
@Schema(description = "Cambio de estado realizado por el tutor propietario de la reserva.")
public class ReservaEstadoUpdateDTO {

    @NotBlank(message = "{validation.reserva.estado.notBlank}")
    @Pattern(regexp = "PENDIENTE|CONFIRMADA|CANCELADA", message = "{validation.reserva.estado.pattern}")
    @Schema(description = "Nuevo estado de la reserva.", example = "CONFIRMADA", allowableValues = {"PENDIENTE", "CONFIRMADA", "CANCELADA"})
    private String estado;
}
