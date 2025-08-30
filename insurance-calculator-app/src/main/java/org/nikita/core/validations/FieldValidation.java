package org.nikita.core.validations;

import org.nikita.api.dto.ValidationErrorDto;

import java.util.List;
import java.util.Optional;

public interface FieldValidation<T> {
    Optional<ValidationErrorDto> validate(T command);

    List<ValidationErrorDto> validateList(T command);
}
