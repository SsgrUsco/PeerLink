package co.edu.usco.peerlink.exception;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.Map;

@Builder
public record ApiErrorResponse(
        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        Map<String, String> details
) {
}
