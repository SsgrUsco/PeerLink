package co.edu.usco.peerlink.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TutorMateriaDTO {

    @NotNull(message = "{validation.tutorMateria.tutorId.notNull}")
    private Integer tutorId;

    @NotNull(message = "{validation.tutorMateria.materiaId.notNull}")
    private Integer materiaId;
}
