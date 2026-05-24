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
}
