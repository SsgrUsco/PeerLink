package co.edu.usco.peerlink.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {

    private final String messageKey;
    private final HttpStatus status;
    private final transient Object[] args;

    public BusinessException(String messageKey, HttpStatus status, Object... args) {
        super(messageKey);
        this.messageKey = messageKey;
        this.status = status;
        this.args = args;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Object[] getArgs() {
        return args;
    }
}
