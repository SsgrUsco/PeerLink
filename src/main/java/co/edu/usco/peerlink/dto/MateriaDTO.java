package co.edu.usco.peerlink.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MateriaDTO {
    private Integer id;

    @NotBlank(message = "{validation.materia.nombre.notBlank}")
    @Pattern(regexp = "^[\\p{L}0-9 ]+$", message = "{validation.materia.nombre.pattern}")
    private String nombre;

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
