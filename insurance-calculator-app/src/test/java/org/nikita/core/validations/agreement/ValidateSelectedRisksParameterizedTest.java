package org.nikita.core.validations.agreement;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.validations.ValidationErrorFactory;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateSelectedRisksParameterizedTest {

    @Mock
    private ValidationErrorFactory errorCodeProperties;

    @InjectMocks
    private ValidateSelectedRisks validateSelectedRisks;

    @Mock
    private TravelCalculatePremiumCoreCommand request;

    @Mock
    private AgreementDto agreementDto;

    private static final String ERROR_CODE_NO_RISKS = "ERROR_CODE_24";
    private static final String ERROR_CODE_UNSUPPORTED_RISK = "ERROR_CODE_20";

    static Stream<TestCase> provideTestCases() {
        return Stream.of(
                new TestCase(
                        List.of(),
                        List.of(new ValidationErrorDto("selectedRisks", "No selected risks")),
                        ERROR_CODE_NO_RISKS,
                        "No selected risks"
                ),
                new TestCase(
                        List.of("InvalidRisk1"),
                        List.of(new ValidationErrorDto("selectedRisks", "Risk not supported: InvalidRisk1")),
                        ERROR_CODE_UNSUPPORTED_RISK,
                        "Risk not supported: InvalidRisk1"
                ),
                new TestCase(
                        List.of("InvalidRisk1", "InvalidRisk2"),
                        List.of(
                                new ValidationErrorDto("selectedRisks", "Risk not supported: InvalidRisk1"),
                                new ValidationErrorDto("selectedRisks", "Risk not supported: InvalidRisk2")
                        ),
                        ERROR_CODE_UNSUPPORTED_RISK,
                        "Risk not supported: "
                ),
                new TestCase(
                        List.of("TRAVEL_MEDICAL"),
                        List.of(),
                        null,
                        null
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    void testValidateSelectedRisks(TestCase testCase) {
        when(request.getAgreement()).thenReturn(agreementDto);
        when(agreementDto.getSelectedRisks()).thenReturn(testCase.inputRisks);

        if (testCase.expectedErrors.isEmpty()) {
            // Для пустого списка ошибок не должно вызываться получение описания ошибки
            var errors = validateSelectedRisks.validateList(request);
            assertTrue(errors.isEmpty());
        } else {
            // Мокаем поведение errorCodeProperties для каждого ожидаемого сообщения
            for (ValidationErrorDto error : testCase.expectedErrors) {
                lenient().when(errorCodeProperties.getErrorDescription(eq(testCase.errorCode), anyList()))
                        .thenReturn(error.description());
            }

            var errors = validateSelectedRisks.validateList(request);

            assertEquals(testCase.expectedErrors.size(), errors.size());
            for (int i = 0; i < errors.size(); i++) {
                assertEquals(testCase.expectedErrors.get(i).errorCode(), errors.get(i).errorCode());
                assertTrue(errors.get(i).description().startsWith(testCase.expectedErrors.get(i).description().substring(0, Math.min(20, testCase.expectedErrors.get(i).description().length()))));
            }
        }
    }

    private static class TestCase {
        final List<String> inputRisks;
        final List<ValidationErrorDto> expectedErrors;
        final String errorCode;
        final String errorDescriptionStart;

        TestCase(List<String> inputRisks, List<ValidationErrorDto> expectedErrors, String errorCode, String errorDescriptionStart) {
            this.inputRisks = inputRisks;
            this.expectedErrors = expectedErrors;
            this.errorCode = errorCode;
            this.errorDescriptionStart = errorDescriptionStart;
        }
    }
}
