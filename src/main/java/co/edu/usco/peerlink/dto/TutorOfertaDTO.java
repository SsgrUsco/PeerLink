package co.edu.usco.peerlink.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TutorOfertaDTO {

    @NotNull(message = "{validation.tutorMateria.materiaId.notNull}")
    private Integer materiaId;

    @NotNull(message = "{validation.reserva.fechaHora.notNull}")
    @Future(message = "{validation.reserva.fechaHora.future}")
    private LocalDateTime fechaHora;
}
