package co.edu.usco.peerlink.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Reserva de tutoria creada por un estudiante o publicada como resultado de una oferta aceptada.")
public class ReservaDTO {
    @Schema(description = "Identificador de la reserva.", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;
    @Schema(description = "ID del estudiante asociado.", example = "3")
    private Integer estudianteId;

    @Schema(description = "ID del tutor asociado.", example = "5")
    private Integer tutorId;

    @Schema(description = "ID de la materia reservada.", example = "1")
    private Integer materiaId;

    @Schema(description = "Fecha y hora programada para la tutoria.", example = "2026-06-01T14:00:00")
    private LocalDateTime fechaHora;

    @Schema(description = "Idioma tecnico de la tutoria.", example = "es", allowableValues = {"es", "en", "pt"})
    private String idioma;

    @Schema(
            description = "Clave tecnica de facultad de la tutoria.",
            example = "INGENIERIA",
            allowableValues = {
                    "CIENCIAS_EXACTAS_Y_NATURALES",
                    "CIENCIAS_JURIDICAS_Y_POLITICAS",
                    "CIENCIAS_SOCIALES_Y_HUMANAS",
                    "ECONOMIA_Y_ADMINISTRACION",
                    "EDUCACION",
                    "INGENIERIA",
                    "SALUD"
            }
    )
    private String facultad;

    @Schema(description = "Estado actual de la reserva.", example = "PENDIENTE", allowableValues = {"PENDIENTE", "CONFIRMADA", "CANCELADA"})
    private String estado;
}
