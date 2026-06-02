package co.edu.usco.peerlink.exception;

import org.springframework.http.HttpStatus;

/**
 * Excepcion de negocio con clave i18n y codigo HTTP asociado.
 */
public class BusinessException extends RuntimeException {

    private final String messageKey;
    private final HttpStatus status;
    private final transient Object[] args;

    /**
     * Crea una excepcion de negocio traducible.
     *
     * @param messageKey clave del mensaje en los bundles i18n
     * @param status estado HTTP que debe retornarse
     * @param args argumentos opcionales del mensaje
     */
    public BusinessException(String messageKey, HttpStatus status, Object... args) {
        super(messageKey);
        this.messageKey = messageKey;
        this.status = status;
        this.args = args;
    }

    /**
     * @return clave i18n del mensaje
     */
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * @return estado HTTP asociado al error
     */
    public HttpStatus getStatus() {
        return status;
    }

    /**
     * @return argumentos del mensaje traducible
     */
    public Object[] getArgs() {
        return args;
    }
}
