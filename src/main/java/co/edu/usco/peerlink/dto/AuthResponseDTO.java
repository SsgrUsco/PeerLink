package co.edu.usco.peerlink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String tipo;
    private String rol;
    private String correo;
    private String nombreCompleto;
}
