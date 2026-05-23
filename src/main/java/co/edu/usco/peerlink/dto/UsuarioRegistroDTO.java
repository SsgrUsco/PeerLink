package co.edu.usco.peerlink.dto;

import lombok.Data;

@Data
public class UsuarioRegistroDTO {
    private String nombreCompleto;
    private String correo;
    private String password;

    /* * Nota: No pedimos el ID porque la base de datos lo genera (autoincremental),
     * y el rol lo asignaremos por defecto en la lógica de negocio.
     */
}