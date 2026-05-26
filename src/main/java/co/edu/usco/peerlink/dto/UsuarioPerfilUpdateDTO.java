package co.edu.usco.peerlink.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Datos editables del perfil del usuario autenticado.")
public class UsuarioPerfilUpdateDTO {

    @NotBlank(message = "{validation.usuario.nombre.notBlank}")
    @Schema(example = "Ana Gomez")
    private String nombreCompleto;

    @NotBlank(message = "{validation.usuario.correo.notBlank}")
    @Email(message = "{validation.usuario.correo.email}")
    @Schema(example = "ana@peerlink.edu.co")
    private String correo;
}
