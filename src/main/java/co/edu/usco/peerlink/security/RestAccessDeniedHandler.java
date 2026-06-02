package co.edu.usco.peerlink.security;

import co.edu.usco.peerlink.exception.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Map;

/**
 * Devuelve respuestas JSON uniformes cuando un usuario autenticado no tiene permisos.
 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();
    private final MessageSource messageSource;

    /**
     * @param messageSource fuente de mensajes i18n para el error 403
     */
    public RestAccessDeniedHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    /**
     * Escribe la respuesta 403 en formato JSON.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @param accessDeniedException excepcion de acceso denegado
     * @throws IOException si falla la escritura de la respuesta
     * @throws ServletException si el contenedor no puede completar el manejo
     */
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ApiErrorResponse body = ApiErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .message(messageSource.getMessage("error.auth.accessDenied", null, LocaleContextHolder.getLocale()))
                .details(Map.of())
                .build();

        objectMapper.writeValue(response.getWriter(), body);
    }
}
