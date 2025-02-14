package com.associate.notify.exception;

/**
 * @author Albert Gomes Cabral
 */
public class NotifyNotFound extends RuntimeException {

    public NotifyNotFound(String message) {
        super(message);
    }

    public NotifyNotFound(Throwable cause) {
        super(cause);
    }

}
