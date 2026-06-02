package co.edu.usco.peerlink.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * Utilidad central para crear y validar tokens JWT de PeerLink.
 *
 * <p>Incluye en el token el correo como subject y claims adicionales como
 * identificador, rol y nombre completo. La firma se realiza con una clave
 * HMAC configurada en propiedades.</p>
 */
@Component
public class JwtUtil {

    private final SecretKey signingKey;
    private final Duration expirationDuration;

    public JwtUtil(@Value("${security.jwt.secret}") String secret,
                   @Value("${security.jwt.expiration-minutes:480}") long expirationMinutes) {
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expirationDuration = Duration.ofMinutes(expirationMinutes);
    }

    /**
     * Devuelve la duracion configurada para la sesion JWT.
     *
     * @return duracion de expiracion del token
     */
    public Duration getExpirationDuration() {
        return expirationDuration;
    }

    /**
     * Genera un token firmado para el usuario autenticado.
     *
     * @param user usuario validado por Spring Security
     * @return JWT compacto firmado
     */
    public String generateToken(AuthenticatedUser user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(user.getUsername())
                .claims(Map.of(
                        "userId", user.getId(),
                        "rol", user.getRol(),
                        "nombreCompleto", user.getNombreCompleto()
                ))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expirationDuration)))
                .signWith(signingKey)
                .compact();
    }

    /**
     * Extrae el correo almacenado como subject del JWT.
     *
     * @param token JWT recibido
     * @return correo del usuario
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae un claim especifico usando una funcion resolutora.
     *
     * @param token JWT recibido
     * @param resolver funcion que obtiene el valor desde los claims
     * @param <T> tipo del valor solicitado
     * @return valor extraido del token
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    /**
     * Valida que el token pertenezca al usuario y no haya expirado.
     *
     * @param token JWT recibido
     * @param userDetails usuario cargado desde la base de datos
     * @return {@code true} cuando el token es valido para el usuario
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
