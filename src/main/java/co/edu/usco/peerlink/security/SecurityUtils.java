package co.edu.usco.peerlink.security;

import co.edu.usco.peerlink.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utilidades para consultar el usuario autenticado desde el contexto de seguridad.
 */
public final class SecurityUtils {

    /**
     * Evita instancias de la clase utilitaria.
     */
    private SecurityUtils() {
    }

    /**
     * Obtiene el usuario autenticado actual.
     *
     * @return usuario autenticado
     * @throws BusinessException cuando no hay usuario valido en el contexto
     */
    public static AuthenticatedUser currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AuthenticatedUser user) {
            return user;
        }
        throw new BusinessException("error.auth.unauthorized", HttpStatus.UNAUTHORIZED);
    }

    /**
     * Retorna el correo autenticado o {@code system} cuando no existe contexto.
     *
     * @return nombre de usuario actual o marcador de sistema
     */
    public static String currentUsernameOrSystem() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getPrincipal() instanceof AuthenticatedUser user
                ? user.getUsername()
                : "system";
    }
}
