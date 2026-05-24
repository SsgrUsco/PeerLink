package co.edu.usco.peerlink.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class TutorMateriaDTO {

    @NotNull(message = "{validation.tutorMateria.tutorId.notNull}")
    private Integer tutorId;

    @NotNull(message = "{validation.tutorMateria.materiaId.notNull}")
    private Integer materiaId;

    @NotBlank(message = "{validation.reserva.idioma.notBlank}")
    @Pattern(regexp = "es|en|pt", message = "{validation.reserva.idioma.pattern}")
    private String idioma;

    @NotBlank(message = "{validation.reserva.facultad.notBlank}")
    @Pattern(
            regexp = "CIENCIAS_EXACTAS_Y_NATURALES|CIENCIAS_JURIDICAS_Y_POLITICAS|CIENCIAS_SOCIALES_Y_HUMANAS|ECONOMIA_Y_ADMINISTRACION|EDUCACION|INGENIERIA|SALUD",
            message = "{validation.reserva.facultad.pattern}"
    )
    private String facultad;
}
