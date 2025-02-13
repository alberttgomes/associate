package com.associate.benefit.api.exception;

/**
 * @author Albert Gomes Cabral
 */
public class BenefitNotFound extends RuntimeException {

    public BenefitNotFound(String message) {
        super(message);
    }

    public BenefitNotFound(Throwable cause) {
        super(cause);
    }

}
