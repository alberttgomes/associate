package com.associate.associate.api.exception;

/**
 * @author Albert Gomes Cabral
 */
public class AssociateNotFound extends RuntimeException {

    public AssociateNotFound(String message) {
        super(message);
    }

    public AssociateNotFound(Throwable cause) {
        super(cause);
    }

    public AssociateNotFound(String message, Throwable cause) {
        super(message, cause);
    }

}
