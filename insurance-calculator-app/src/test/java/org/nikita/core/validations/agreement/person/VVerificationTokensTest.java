package org.nikita.core.validations.agreement.person;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.PersonDto;
import org.nikita.core.domain.VerificationToken;
import org.nikita.core.validations.ValidationErrorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VVerificationTokensTest {

    private final ValidationErrorFactory errorFactory = new ValidationErrorFactory();
    private final VVerificationTokens validator = new VVerificationTokens(errorFactory);

    static Stream<TestCase> verificationTokensProvider() {
        return Stream.of(
                new TestCase(null, true),
                new TestCase(new ArrayList<>(), false),
                new TestCase(List.of(new VerificationToken()), false)
        );
    }

    @ParameterizedTest
    @MethodSource("verificationTokensProvider")
    void testVerificationTokensValidation(TestCase testCase) {
        PersonDto person = new PersonDto();
        person.setVerificationTokens( testCase.tokens);

        AgreementDto agreement = new AgreementDto();
        agreement.getPersons().add(person);

        TravelCalculatePremiumCoreCommand command = new TravelCalculatePremiumCoreCommand();
        command.setAgreement(agreement);

        var result = validator.validate(command);

        if (testCase.expectError) {
            assertTrue(result.isPresent(), "Expected validation error for tokens: " + testCase.tokens);
            assertEquals("verificationTokens", result.get().errorCode());
        } else {
            assertTrue(result.isEmpty(), "Did not expect validation error for tokens: " + testCase.tokens);
        }
    }

    static class TestCase {
        List<VerificationToken> tokens;
        boolean expectError;

        TestCase(List<VerificationToken> tokens, boolean expectError) {
            this.tokens = tokens;
            this.expectError = expectError;
        }
    }
}
