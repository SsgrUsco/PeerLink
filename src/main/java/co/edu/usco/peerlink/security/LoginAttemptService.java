package co.edu.usco.peerlink.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Controla intentos fallidos de inicio de sesion para mitigar ataques de fuerza bruta.
 */
@Service
public class LoginAttemptService {

    private final ConcurrentMap<String, AttemptState> attempts = new ConcurrentHashMap<>();
    private final int maxAttempts;
    private final Duration blockDuration;

    /**
     * @param maxAttempts cantidad maxima de fallos permitidos antes del bloqueo
     * @param blockMinutes duracion del bloqueo temporal en minutos
     */
    public LoginAttemptService(@Value("${security.login.max-attempts:5}") int maxAttempts,
                               @Value("${security.login.block-minutes:15}") long blockMinutes) {
        this.maxAttempts = maxAttempts;
        this.blockDuration = Duration.ofMinutes(blockMinutes);
    }

    /**
     * Verifica si la combinacion correo/IP se encuentra temporalmente bloqueada.
     *
     * @param correo correo intentado
     * @param remoteAddress direccion remota del cliente
     * @return {@code true} si aun esta bloqueada
     */
    public boolean isBlocked(String correo, String remoteAddress) {
        String attemptKey = key(correo, remoteAddress);
        AttemptState state = attempts.get(attemptKey);
        if (state == null) {
            return false;
        }
        if (state.blockedUntil != null && state.blockedUntil.isAfter(Instant.now())) {
            return true;
        }
        if (state.blockedUntil != null) {
            attempts.remove(attemptKey);
        }
        return false;
    }

    /**
     * Limpia los intentos fallidos tras un inicio de sesion correcto.
     *
     * @param correo correo autenticado
     * @param remoteAddress direccion remota del cliente
     */
    public void recordSuccess(String correo, String remoteAddress) {
        attempts.remove(key(correo, remoteAddress));
    }

    /**
     * Registra un intento fallido y activa el bloqueo si supera el umbral.
     *
     * @param correo correo intentado
     * @param remoteAddress direccion remota del cliente
     */
    public void recordFailure(String correo, String remoteAddress) {
        attempts.compute(key(correo, remoteAddress), (ignored, current) -> {
            AttemptState state = current == null ? new AttemptState() : current;
            state.failures++;
            if (state.failures >= maxAttempts) {
                state.blockedUntil = Instant.now().plus(blockDuration);
            }
            return state;
        });
    }

    /**
     * Construye una clave estable para agrupar intentos por correo e IP.
     *
     * @param correo correo intentado
     * @param remoteAddress direccion remota
     * @return clave normalizada
     */
    private String key(String correo, String remoteAddress) {
        return "%s|%s".formatted(
                correo == null ? "" : correo.trim().toLowerCase(Locale.ROOT),
                remoteAddress == null ? "" : remoteAddress
        );
    }

    private static final class AttemptState {
        private int failures;
        private Instant blockedUntil;
    }
}
