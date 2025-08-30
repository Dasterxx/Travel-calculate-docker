package org.nikita.core.underwriting;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.RiskDto;
import org.nikita.core.underwriting.calcs.AgeCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SelectedRisksPremiumCalculatorTest {

    @Mock
    private AgeCalculator ageCalculator;

    @Mock
    private List<TravelRiskPremiumCalculator> riskCalculators;

    @InjectMocks
    private SelectedRisksPremiumCalculator calculator;

    @ParameterizedTest
    @MethodSource("providePremiumCalculationData")
    void shouldCalculateRiskWithCoefficients(
            String risk,
            LocalDate personBirthDate,
            List<String> countriesToVisit,
            BigDecimal insuranceLimit,
            BigDecimal basePremium,
            BigDecimal ageCoefficientValue,
            BigDecimal countryCoefficientValue,
            BigDecimal insuranceLimitCoefficientValue,
            BigDecimal expectedPremium
    ) {
        AgreementDto request = new AgreementDto();
        request.setSelectedRisks(Collections.singletonList(risk));

        PersonDto person = new PersonDto("John", "Doe", personBirthDate);
        request.setPersons(List.of(person));

        request.setCountriesToVisit(countriesToVisit);
        request.setInsuranceLimit(insuranceLimit);

        TravelRiskPremiumCalculator riskCalculator = mock(TravelRiskPremiumCalculator.class);
        when(riskCalculator.getRiskIc()).thenReturn(risk);
        when(riskCalculator.calculatePremium(any(), any())).thenReturn(basePremium);

        when(riskCalculators.stream()).thenReturn(Stream.of(riskCalculator));

        List<RiskDto> result = calculator.calculatePremiumForAllRisks(request, person);

        assertNotNull(result, "Result list should not be null");
        assertEquals(1, result.size(), "Result list should contain exactly one element");
        RiskDto riskDto = result.getFirst();
        assertEquals(expectedPremium.setScale(2, RoundingMode.HALF_UP),
                riskDto.getPremium().setScale(2, RoundingMode.HALF_UP), "Calculated premium should match the expected value");
    }

    private static Stream<Arguments> providePremiumCalculationData() {
        return Stream.of(
                Arguments.of(
                        "TEST_RISK",
                        "1994-01-01",
                        List.of("USA"),
                        BigDecimal.valueOf(15000),
                        BigDecimal.valueOf(100),
                        BigDecimal.valueOf(1.3),
                        BigDecimal.valueOf(1.5),
                        BigDecimal.valueOf(1.5),
                        BigDecimal.valueOf(100.00)
                ),
                Arguments.of(
                        "ANOTHER_RISK",
                        "1984-01-01",
                        List.of("Spain"),
                        BigDecimal.valueOf(20000),
                        BigDecimal.valueOf(200),
                        BigDecimal.valueOf(1.5),
                        BigDecimal.valueOf(1.2),
                        BigDecimal.valueOf(2.0),
                        BigDecimal.valueOf(200.00)
                )
        );
    }
}

