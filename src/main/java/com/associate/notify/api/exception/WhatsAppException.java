package com.associate.notify.api.exception;

/**
 * @author Albert Gomes Cabral
 */
public class WhatsAppException extends RuntimeException {

    public WhatsAppException(String message) {
        super(message);
    }

    public WhatsAppException(String message, Throwable cause) {
        super(message, cause);
    }

}
