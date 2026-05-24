package co.edu.usco.peerlink.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthDTO {

    @NotBlank(message = "{validation.auth.correo.notBlank}")
    @Email(message = "{validation.auth.correo.email}")
    private String correo;

    @NotBlank(message = "{validation.auth.password.notBlank}")
    private String password;
}
