package co.edu.usco.peerlink.exception;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manejador global que transforma excepciones del backend en respuestas JSON uniformes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    /**
     * @param messageSource fuente de mensajes para internacionalizacion de errores
     */
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    /**
     * Convierte errores de validacion Bean Validation en respuesta 400.
     *
     * @param exception excepcion de validacion
     * @return respuesta con detalles por campo
     */
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> details = new LinkedHashMap<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            details.put(fieldError.getField(), messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()));
        }

        return ResponseEntity.badRequest().body(ApiErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(resolveMessage("error.validation"))
                .details(details)
                .build());
    }

    @ExceptionHandler(BusinessException.class)
    /**
     * Convierte excepciones de negocio en el estado HTTP definido por el servicio.
     *
     * @param exception excepcion de negocio
     * @return respuesta traducida para el cliente
     */
    public ResponseEntity<ApiErrorResponse> handleBusiness(BusinessException exception) {
        HttpStatus status = exception.getStatus();
        return ResponseEntity.status(status).body(ApiErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(resolveMessage(exception.getMessageKey(), exception.getArgs()))
                .details(Map.of())
                .build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    /**
     * Responde a credenciales invalidas sin revelar informacion sensible.
     *
     * @param exception excepcion de autenticacion
     * @return respuesta 401
     */
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(BadCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(resolveMessage("error.auth.invalidCredentials"))
                .details(Map.of())
                .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    /**
     * Responde a intentos autenticados sin permisos suficientes.
     *
     * @param exception excepcion de autorizacion
     * @return respuesta 403
     */
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .message(resolveMessage("error.auth.accessDenied"))
                .details(Map.of())
                .build());
    }

    @ExceptionHandler(Exception.class)
    /**
     * Captura errores no controlados y devuelve un mensaje generico.
     *
     * @param exception excepcion inesperada
     * @return respuesta 500
     */
    public ResponseEntity<ApiErrorResponse> handleGeneral(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(resolveMessage("error.internal"))
                .details(Map.of())
                .build());
    }

    /**
     * Resuelve una clave i18n con el idioma actual de la solicitud.
     *
     * @param key clave del mensaje
     * @param args argumentos opcionales
     * @return mensaje localizado
     */
    private String resolveMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, key, LocaleContextHolder.getLocale());
    }
}
