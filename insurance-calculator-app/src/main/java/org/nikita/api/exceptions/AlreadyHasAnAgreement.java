package org.nikita.api.exceptions;

public class AlreadyHasAnAgreement extends RuntimeException {
    public AlreadyHasAnAgreement(String message) {
        super(message);
    }
}
