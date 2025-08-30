package org.nikita.api.command.registration;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RegistrationCommandTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

    @Test
    void whenAllFieldsValid_thenNoViolations() {
        RegistrationCommand command = RegistrationCommand.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("StrongPass123")
                .birthDate(LocalDate.of(1990, 1, 1))
                .gender("Male")
                .isEnabled(true)
                .build();

        Set<ConstraintViolation<RegistrationCommand>> violations = validator.validate(command);

        assertThat(violations).isEmpty();
    }

    @Test
    void whenInvalidEmail_thenViolation() {
        RegistrationCommand command = RegistrationCommand.builder()
                .firstName("John")
                .lastName("Doe")
                .email("invalid-email")
                .password("StrongPass123")
                .birthDate(LocalDate.of(1990, 1, 1))
                .gender("Male")
                .isEnabled(true)
                .build();

        Set<ConstraintViolation<RegistrationCommand>> violations = validator.validate(command);

        assertThat(violations)
                .extracting("propertyPath")
                .extracting(Object::toString)
                .contains("email");
    }

    @Test
    void whenBlankFirstName_thenViolation() {
        RegistrationCommand command = RegistrationCommand.builder()
                .firstName("")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("StrongPass123")
                .birthDate(LocalDate.of(1990, 1, 1))
                .gender("Male")
                .isEnabled(true)
                .build();

        Set<ConstraintViolation<RegistrationCommand>> violations = validator.validate(command);

        assertThat(violations)
                .extracting("propertyPath")
                .extracting(Object::toString)
                .contains("firstName");
    }

    @Test
    void whenNullBirthDate_thenViolation() {
        RegistrationCommand command = RegistrationCommand.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("StrongPass123")
                .birthDate(null)
                .gender("Male")
                .isEnabled(true)
                .build();

        Set<ConstraintViolation<RegistrationCommand>> violations = validator.validate(command);

        assertThat(violations)
                .extracting("propertyPath")
                .extracting(Object::toString)
                .contains("birthDate");
    }

    @Test
    void whenInvalidGender_thenViolation() {
        RegistrationCommand command = RegistrationCommand.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("StrongPass123")
                .birthDate(LocalDate.of(1990, 1, 1))
                .gender("Unknown")
                .isEnabled(true)
                .build();

        Set<ConstraintViolation<RegistrationCommand>> violations = validator.validate(command);

        assertThat(violations)
                .extracting("propertyPath")
                .extracting(Object::toString)
                .contains("gender");
    }
}