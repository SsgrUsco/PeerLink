package co.edu.usco.peerlink.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO general de usuario usado para respuestas y operaciones administrativas.
 */
@Data
@Schema(description = "Usuario del sistema mapeado desde tablas satelite 6NF.")
public class UsuarioDTO {
    @Schema(example = "3", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotBlank(message = "{validation.usuario.nombre.notBlank}")
    @Schema(example = "Ana Gomez")
    private String nombreCompleto;

    @NotBlank(message = "{validation.usuario.correo.notBlank}")
    @Email(message = "{validation.usuario.correo.email}")
    @Schema(example = "ana@peerlink.edu.co")
    private String correo;

    @Schema(
            description = "Solo se recibe en operaciones de creacion. No debe exponerse en listados.",
            example = "Seguro1234",
            writeOnly = true
    )
    private String password;

    @Schema(example = "ESTUDIANTE", allowableValues = {"ADMIN", "ESTUDIANTE", "TUTOR"})
    private String rol;
}
