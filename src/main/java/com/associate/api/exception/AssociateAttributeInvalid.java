package com.associate.api.exception;

/**
 * @author Albert Gomes Cabral
 */
public class AssociateAttributeInvalid extends RuntimeException {

    public AssociateAttributeInvalid(String message) {
        super(message);
    }

    public AssociateAttributeInvalid(Throwable cause) {
        super(cause);
    }

    public AssociateAttributeInvalid(String message, Throwable cause) {
        super(message, cause);
    }

}
