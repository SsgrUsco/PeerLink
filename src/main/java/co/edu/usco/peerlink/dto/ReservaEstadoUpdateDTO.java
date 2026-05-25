package co.edu.usco.peerlink.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ReservaEstadoUpdateDTO {

    @NotBlank(message = "{validation.reserva.estado.notBlank}")
    @Pattern(regexp = "PENDIENTE|CONFIRMADA|CANCELADA", message = "{validation.reserva.estado.pattern}")
    private String estado;
}
