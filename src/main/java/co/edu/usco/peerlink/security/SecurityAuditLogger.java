package co.edu.usco.peerlink.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Registra eventos de seguridad y administracion del sistema.
 *
 * <p>Escribe eventos en el logger {@code SECURITY_AUDIT}. La auditoria
 * persistente de entidades principales se maneja directamente en PostgreSQL
 * mediante triggers definidos en los scripts SQL del proyecto.</p>
 */
@Component
public class SecurityAuditLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger("SECURITY_AUDIT");

    public void log(String event, String actor, String detail) {
        String safeEvent = safe(event);
        String safeActor = safe(actor);
        String safeDetail = safe(detail);
        LOGGER.info("event={} actor={} detail={}", safeEvent, safeActor, safeDetail);
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "-" : value.replaceAll("[\\r\\n\\t]", "_");
    }
}
