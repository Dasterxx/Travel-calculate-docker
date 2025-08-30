package org.nikita.core.validations.agreement.person;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.PersonDto;
import org.nikita.core.validations.ValidationErrorFactory;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class VRolesTest {

    private final ValidationErrorFactory errorFactory = new ValidationErrorFactory();
    private final VRoles validator = new VRoles(errorFactory);

    static Stream<TestCase> roleProvider() {
        return Stream.of(
                new TestCase(null, true),
                new TestCase(List.of(), true),
                new TestCase(List.of("ROLE_GOD"), true),
                new TestCase(List.of("ROLE_USER"), false),
                new TestCase(List.of("ROLE_ADMIN"), false),
                new TestCase(List.of("ROLE_USER", "ROLE_ADMIN"), false),
                new TestCase(List.of("ROLE_USER", "ROLE_UNKNOWN"), true)
        );
    }

    @ParameterizedTest
    @MethodSource("roleProvider")
    void testRolesValidation(TestCase testCase) {
        PersonDto person = new PersonDto();
        person.setRoles(testCase.roles);

        AgreementDto agreement = new AgreementDto();
        agreement.getPersons().add(person);

        TravelCalculatePremiumCoreCommand command = new TravelCalculatePremiumCoreCommand();
        command.setAgreement(agreement);

        var result = validator.validate(command);

        if (testCase.expectError) {
            assertTrue(result.isPresent(), "Expected validation error for roles: " + testCase.roles);
            assertEquals("roles", result.get().errorCode());
        } else {
            assertTrue(result.isEmpty(), "Did not expect validation error for roles: " + testCase.roles);
        }
    }

    static class TestCase {
        Collection<String> roles;
        boolean expectError;

        TestCase(Collection<String> roles, boolean expectError) {
            this.roles = roles;
            this.expectError = expectError;
        }
    }
}
