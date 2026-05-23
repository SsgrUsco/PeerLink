package co.edu.usco.peerlink.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Integer id;
    private String nombreCompleto;
    private String correo;
    private String password;
    private String rol;
}