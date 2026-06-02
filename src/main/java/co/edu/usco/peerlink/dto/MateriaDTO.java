package co.edu.usco.peerlink.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Describe una materia del catalogo junto con sus claves tecnicas de idioma y facultad.
 */
@Data
@Schema(description = "Materia academica con claves tecnicas de idioma y facultad asociadas al catalogo.")
public class MateriaDTO {
    @Schema(description = "Identificador interno de la materia.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotBlank(message = "{validation.materia.nombre.notBlank}")
    @Pattern(regexp = "^[\\p{L}0-9 ]+$", message = "{validation.materia.nombre.pattern}")
    @Schema(description = "Nombre visible de la materia.", example = "Calculo diferencial")
    private String nombre;

    @NotBlank(message = "{validation.reserva.idioma.notBlank}")
    @Pattern(regexp = "es|en|pt", message = "{validation.reserva.idioma.pattern}")
    @Schema(description = "Idioma tecnico asociado a la materia.", example = "es", allowableValues = {"es", "en", "pt"})
    private String idioma;

    @NotBlank(message = "{validation.reserva.facultad.notBlank}")
    @Pattern(
            regexp = "CIENCIAS_EXACTAS_Y_NATURALES|CIENCIAS_JURIDICAS_Y_POLITICAS|CIENCIAS_SOCIALES_Y_HUMANAS|ECONOMIA_Y_ADMINISTRACION|EDUCACION|INGENIERIA|SALUD",
            message = "{validation.reserva.facultad.pattern}"
    )
    @Schema(
            description = "Clave tecnica de la facultad asociada a la materia.",
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
}
