package co.edu.usco.peerlink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@Schema(description = "Respuesta de autenticacion con token JWT y datos basicos del usuario.")
public class AuthResponseDTO {
    @Schema(description = "Token JWT que debe enviarse como Bearer token.", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;
    @Schema(example = "Bearer")
    private String tipo;
    @Schema(example = "ADMIN", allowableValues = {"ADMIN", "ESTUDIANTE", "TUTOR"})
    private String rol;
    @Schema(example = "admin@peerlink.edu.co")
    private String correo;
    @Schema(example = "Super Administrador")
    private String nombreCompleto;
}
