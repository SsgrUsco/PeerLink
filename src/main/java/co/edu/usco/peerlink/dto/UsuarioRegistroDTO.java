package co.edu.usco.peerlink.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UsuarioRegistroDTO {

    @NotBlank(message = "{validation.usuario.nombre.notBlank}")
    private String nombreCompleto;

    @NotBlank(message = "{validation.usuario.correo.notBlank}")
    @Email(message = "{validation.usuario.correo.email}")
    private String correo;

    @NotBlank(message = "{validation.usuario.password.notBlank}")
    private String password;

    @NotBlank(message = "{validation.usuario.rol.notBlank}")
    @Pattern(regexp = "ESTUDIANTE|TUTOR", message = "{validation.usuario.rol.pattern}")
    private String rol;
}
