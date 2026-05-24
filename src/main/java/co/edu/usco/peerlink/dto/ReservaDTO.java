package co.edu.usco.peerlink.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservaDTO {
    private Integer id;
    private Integer estudianteId;

    @NotNull(message = "{validation.reserva.tutorId.notNull}")
    private Integer tutorId;

    @NotNull(message = "{validation.reserva.materiaId.notNull}")
    private Integer materiaId;

    @NotNull(message = "{validation.reserva.fechaHora.notNull}")
    @Future(message = "{validation.reserva.fechaHora.future}")
    private LocalDateTime fechaHora;

    private String idioma;

    private String facultad;

    private String estado;
}
