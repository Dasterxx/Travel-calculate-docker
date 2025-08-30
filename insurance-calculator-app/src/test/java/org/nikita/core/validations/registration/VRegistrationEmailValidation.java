package org.nikita.core.validations.registration;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.nikita.api.command.registration.RegistrationCommand;
import org.nikita.core.validations.ValidationErrorFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VRegistrationEmailValidation {

    private final ValidationErrorFactory errorFactory = new ValidationErrorFactory();
    private final RegistrationEmailValidation validator = new RegistrationEmailValidation(errorFactory);

    static Stream<TestCase> emailProvider() {
        return Stream.of(
                new TestCase(null, true),
                new TestCase("", true),
                new TestCase("31221", true),
                new TestCase("test@google.com", false)
        );
    }

    @ParameterizedTest
    @MethodSource("emailProvider")
    void testPasswordValidation(TestCase testCase) {
        RegistrationCommand command = new RegistrationCommand();
        command.setEmail(testCase.email);

        var result = validator.validate(command);

        if (testCase.expectError) {
            assertTrue(result.isPresent(), "Expected validation error for email: " + testCase.email);
            assertEquals("email", result.get().errorCode());
        } else {
            assertTrue(result.isEmpty(), "Did not expect validation error for email: " + testCase.email);
        }
    }

    static class TestCase {
        String email;
        boolean expectError;

        TestCase(String email, boolean expectError) {
            this.email = email;
            this.expectError = expectError;
        }
    }
}