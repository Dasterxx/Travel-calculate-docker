package org.nikita.api.exceptions;

import lombok.Getter;
import org.nikita.api.dto.ValidationErrorDto;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {
    private final List<ValidationErrorDto> errors;

    public ValidationException(String message, List<ValidationErrorDto> errors) {
        super(message);
        this.errors = errors;
    }
}

