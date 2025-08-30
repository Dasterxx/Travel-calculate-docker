package org.nikita.core.underwriting.calcs.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.RiskDto;
import org.nikita.core.underwriting.ITravelPremiumUnderwriting;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TravelPremiumCalculatorTest {

    @Mock
    private ITravelPremiumUnderwriting underwriting;

    private TravelPremiumCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new TravelPremiumCalculator(underwriting);
    }

    @Test
    void shouldCalculateTotalPremiumCorrectly() {
        AgreementDto agreement = new AgreementDto();
        PersonDto person1 = createPersonWithRisks(BigDecimal.valueOf(100), BigDecimal.valueOf(50));
        PersonDto person2 = createPersonWithRisks(BigDecimal.valueOf(75), BigDecimal.valueOf(25));
        agreement.setPersons(List.of(person1, person2));

        when(underwriting.calculateRiskPremiums(agreement, person1)).thenReturn(person1.getRisks());
        when(underwriting.calculateRiskPremiums(agreement, person2)).thenReturn(person2.getRisks());

        calculator.calculatePremiums(agreement);

        assertEquals(BigDecimal.valueOf(250), agreement.getAgreementPremium());
    }

    private PersonDto createPersonWithRisks(BigDecimal... premiums) {
        PersonDto person = new PersonDto();
        person.setRisks(
                Arrays.stream(premiums)
                        .map(p -> new RiskDto("TEST", p))
                        .toList()
        );
        return person;
    }
}
