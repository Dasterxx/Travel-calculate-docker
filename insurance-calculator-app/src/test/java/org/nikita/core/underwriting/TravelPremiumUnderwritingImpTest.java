package org.nikita.core.underwriting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.RiskDto;
import org.nikita.core.util.IDateTimeService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TravelPremiumUnderwritingImpTest {

    @Mock
    private IDateTimeService dateTimeService;

    @Mock
    private SelectedRisksPremiumCalculator riskCalculator;

    @InjectMocks
    private TravelPremiumUnderwritingImp underwriting;

    @Mock
    private AgreementDto request;

    @Test
    void testCalculateTripCost() {
        when(request.getAgreementDateFrom()).thenReturn(LocalDateTime.of(2024, 1, 1, 0, 0));
        when(request.getAgreementDateTo()).thenReturn(LocalDateTime.of(2024, 1, 3, 0, 0));
        when(dateTimeService.getDaysBetween(any(), any())).thenReturn(2L);

        PersonDto person = new PersonDto("John", "Doe", LocalDate.of(1994, 1, 1));
        BigDecimal tripCost = underwriting.calculateTripCost(request, person);

        assertEquals(BigDecimal.valueOf(20.00), tripCost);
    }

    @Test
    void testCalculateInsuranceCost() {
        when(request.getAgreementDateFrom()).thenReturn(LocalDateTime.of(2024, 1, 1, 0, 0));
        when(request.getAgreementDateTo()).thenReturn(LocalDateTime.of(2024, 1, 3, 0, 0));

        PersonDto person = new PersonDto("John", "Doe", LocalDate.of(1994, 1, 1));

        when(riskCalculator.calculatePremiumForAllRisks(any(), any())).thenReturn(
                List.of(new RiskDto("TEST_RISK", BigDecimal.valueOf(100.00)))
        );

        BigDecimal insuranceCost = underwriting.calculateInsuranceCost(request, person);

        assertEquals(BigDecimal.valueOf(100.00), insuranceCost);
    }

    @Test
    void testCalculateRiskPremiums() {
        PersonDto person = new PersonDto("John", "Doe", LocalDate.of(1994, 1, 1));
        when(riskCalculator.calculatePremiumForAllRisks(any(), any())).thenReturn(
                List.of(new RiskDto("TEST_RISK", BigDecimal.valueOf(100.00))
        ));

        List<RiskDto> riskPremiums = underwriting.calculateRiskPremiums(request, person);

        assertNotNull(riskPremiums);
        assertEquals(1, riskPremiums.size());
        assertEquals("TEST_RISK", riskPremiums.getFirst().getRiskIc());
        assertEquals(BigDecimal.valueOf(100.00), riskPremiums.getFirst().getPremium());
    }

    @Test
    void testCalculateTripCostWithInvalidDates() {
        when(request.getAgreementDateFrom()).thenReturn(null);

        PersonDto person = new PersonDto("John", "Doe", LocalDate.of(1994, 1, 1));
        BigDecimal tripCost = underwriting.calculateTripCost(request, person);

        assertEquals(BigDecimal.ZERO, tripCost);
    }

    @Test
    void testCalculateInsuranceCostWithInvalidDates() {
        when(request.getAgreementDateFrom()).thenReturn(null);

        PersonDto person = new PersonDto("John", "Doe", LocalDate.of(1994, 1, 1));
        BigDecimal insuranceCost = underwriting.calculateInsuranceCost(request, person);

        assertEquals(BigDecimal.ZERO, insuranceCost);
    }
}
