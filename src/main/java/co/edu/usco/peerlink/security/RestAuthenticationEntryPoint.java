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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Map;

/**
 * Devuelve respuestas JSON uniformes cuando una solicitud no esta autenticada.
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();
    private final MessageSource messageSource;

    /**
     * @param messageSource fuente de mensajes i18n para el error 401
     */
    public RestAuthenticationEntryPoint(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    /**
     * Escribe la respuesta 401 en formato JSON.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @param authException excepcion de autenticacion
     * @throws IOException si falla la escritura de la respuesta
     * @throws ServletException si el contenedor no puede completar el manejo
     */
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ApiErrorResponse body = ApiErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(messageSource.getMessage("error.auth.unauthorized", null, LocaleContextHolder.getLocale()))
                .details(Map.of())
                .build();

        objectMapper.writeValue(response.getWriter(), body);
    }
}
