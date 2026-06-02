package co.edu.usco.peerlink.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Expone el detalle de una tutoria publicada o asignacion tutor-materia.
 */
@Data
@Schema(description = "Detalle de una relacion tutor-materia, incluyendo horario publicado, idioma y facultad.")
public class TutorMateriaDetalleDTO {
    @Schema(example = "5")
    private Integer tutorId;
    @Schema(example = "Laura Perez")
    private String tutorNombre;
    @Schema(example = "laura@peerlink.edu.co")
    private String tutorCorreo;
    @Schema(example = "1")
    private Integer materiaId;
    @Schema(example = "Calculo diferencial")
    private String materiaNombre;
    @Schema(example = "2026-06-01T14:00:00")
    private java.time.LocalDateTime fechaHora;
    @Schema(example = "es", allowableValues = {"es", "en", "pt"})
    private String idioma;
    @Schema(example = "INGENIERIA")
    private String facultad;
}
