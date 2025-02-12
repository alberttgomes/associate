package com.associate.api.exception;

/**
 * @author Albert Gomes Cabral
 */
public class CompanyNotFound extends RuntimeException {

    public CompanyNotFound(String message) {
        super(message);
    }

    public CompanyNotFound(String message, Throwable cause) {
        super(message, cause);
    }

}
