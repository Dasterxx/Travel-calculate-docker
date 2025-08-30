package org.nikita.core.security.exceptions;

public class SelfDelete extends RuntimeException {
    public SelfDelete(String message) {
        super(message);
    }
}
