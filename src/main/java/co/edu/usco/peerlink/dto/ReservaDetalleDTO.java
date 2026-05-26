package co.edu.usco.peerlink.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Vista detallada de una reserva para horarios, listas y reportes.")
public class ReservaDetalleDTO {
    @Schema(example = "10")
    private Integer id;
    @Schema(example = "3")
    private Integer estudianteId;
    @Schema(example = "Ana Gomez")
    private String estudianteNombre;
    @Schema(example = "5")
    private Integer tutorId;
    @Schema(example = "Laura Perez")
    private String tutorNombre;
    @Schema(example = "1")
    private Integer materiaId;
    @Schema(example = "Calculo diferencial")
    private String materiaNombre;
    @Schema(example = "2026-06-01T14:00:00")
    private LocalDateTime fechaHora;
    @Schema(example = "es", allowableValues = {"es", "en", "pt"})
    private String idioma;
    @Schema(example = "INGENIERIA")
    private String facultad;
    @Schema(example = "PENDIENTE", allowableValues = {"PENDIENTE", "CONFIRMADA", "CANCELADA"})
    private String estado;
}
