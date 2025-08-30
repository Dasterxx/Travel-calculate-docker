package org.nikita.core.validations.registration;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.nikita.api.command.registration.RegistrationCommand;
import org.nikita.core.validations.ValidationErrorFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class VPasswordRegistrationTest {

    private final ValidationErrorFactory errorFactory = new ValidationErrorFactory();
    private final VPasswordRegistration validator = new VPasswordRegistration(errorFactory);

    static Stream<TestCase> passwordProvider() {
        return Stream.of(
                new TestCase(null, true),
                new TestCase("", true),
                new TestCase("short", true),
                new TestCase("validPassword123", false)
        );
    }

    @ParameterizedTest
    @MethodSource("passwordProvider")
    void testPasswordValidation(TestCase testCase) {
        RegistrationCommand command = new RegistrationCommand();
        command.setPassword(testCase.password);

        var result = validator.validate(command);

        if (testCase.expectError) {
            assertTrue(result.isPresent(), "Expected validation error for password: " + testCase.password);
            assertEquals("password", result.get().errorCode());
        } else {
            assertTrue(result.isEmpty(), "Did not expect validation error for password: " + testCase.password);
        }
    }

    static class TestCase {
        String password;
        boolean expectError;

        TestCase(String password, boolean expectError) {
            this.password = password;
            this.expectError = expectError;
        }
    }
}
