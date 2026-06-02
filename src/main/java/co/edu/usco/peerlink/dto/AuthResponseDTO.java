package co.edu.usco.peerlink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Devuelve la informacion basica del usuario autenticado y el token emitido.
 */
@Data
@AllArgsConstructor
@Schema(description = "Respuesta de autenticacion con datos basicos del usuario. El JWT se entrega principalmente en la cookie HttpOnly AUTH_TOKEN.")
public class AuthResponseDTO {
    @Schema(description = "Token JWT opcional. En el flujo web actual se entrega como cookie HttpOnly y este campo puede ser nulo.", example = "eyJhbGciOiJIUzI1NiJ9...", nullable = true)
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
