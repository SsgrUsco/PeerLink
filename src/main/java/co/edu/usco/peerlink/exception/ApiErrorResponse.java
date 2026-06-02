package co.edu.usco.peerlink.exception;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.Map;

/**
 * Estructura uniforme para devolver errores REST al frontend.
 *
 * @param timestamp momento en que ocurrio el error
 * @param status codigo HTTP numerico
 * @param error nombre general del estado HTTP
 * @param message mensaje traducible para el usuario
 * @param details errores de campo u otros detalles especificos
 */
@Builder
public record ApiErrorResponse(
        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        Map<String, String> details
) {
}
