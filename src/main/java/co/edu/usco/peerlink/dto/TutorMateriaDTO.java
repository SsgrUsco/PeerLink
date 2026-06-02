package co.edu.usco.peerlink.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Define la asignacion basica entre un tutor y una materia del catalogo.
 */
@Data
@Schema(description = "Asignacion administrativa de un tutor a una materia existente.")
public class TutorMateriaDTO {

    @NotNull(message = "{validation.tutorMateria.tutorId.notNull}")
    @Schema(example = "5")
    private Integer tutorId;

    @NotNull(message = "{validation.tutorMateria.materiaId.notNull}")
    @Schema(example = "1")
    private Integer materiaId;
}
