package co.edu.usco.peerlink.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Data
@Schema(description = "Datos para que un tutor publique una tutoria disponible sobre una materia existente.")
public class TutorOfertaDTO {

    @NotNull(message = "{validation.tutorMateria.materiaId.notNull}")
    @Schema(description = "ID de la materia que el tutor desea ofrecer.", example = "1")
    private Integer materiaId;

    @NotNull(message = "{validation.reserva.fechaHora.notNull}")
    @Future(message = "{validation.reserva.fechaHora.future}")
    @Schema(description = "Fecha y hora futura en la que se realizara la tutoria.", example = "2026-06-01T14:00:00")
    private LocalDateTime fechaHora;
}
