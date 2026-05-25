package co.edu.usco.peerlink.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioPasswordUpdateDTO {

    @NotBlank
    private String passwordActual;

    @NotBlank
    private String passwordNueva;
}
