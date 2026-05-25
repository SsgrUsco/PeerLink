package co.edu.usco.peerlink.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioPerfilUpdateDTO {

    @NotBlank(message = "{validation.usuario.nombre.notBlank}")
    private String nombreCompleto;

    @NotBlank(message = "{validation.usuario.correo.notBlank}")
    @Email(message = "{validation.usuario.correo.email}")
    private String correo;
}
