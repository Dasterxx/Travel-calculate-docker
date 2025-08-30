package org.nikita.core.underwriting;

import org.junit.jupiter.api.Test;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.RiskDto;
import org.nikita.core.util.AvailableRisk;
import org.nikita.core.util.IDateTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {
        TravelPremiumUnderwritingImp.class,
        SelectedRisksPremiumCalculator.class
})
@TestPropertySource(properties = {
        "medical.risk.limit.level.enabled=true",
        "age.coefficient.level.enabled=true"
})
class TravelUnderwritingIntegrationTest {

    @Autowired
    private TravelPremiumUnderwritingImp underwritingService;

    @MockBean
    private IDateTimeService dateTimeService;

    @MockBean
    private List<TravelRiskPremiumCalculator> riskCalculators;

    @SpyBean
    private SelectedRisksPremiumCalculator riskPremiumCalculator;

    @Test
    void calculateTripCost_ValidDates_ReturnsCorrectValue() {
        AgreementDto agreement = createValidAgreement();
        when(dateTimeService.getDaysBetween(any(), any())).thenReturn(7L);

        BigDecimal result = underwritingService.calculateTripCost(agreement, new PersonDto());

        assertEquals(new BigDecimal("70.0"), result);
    }

    @Test
    void calculateTripCost_NullDates_ReturnsZero() {
        AgreementDto invalidAgreement = new AgreementDto();
        assertEquals(BigDecimal.ZERO,
                underwritingService.calculateTripCost(invalidAgreement, new PersonDto()));
    }


    @Test
    void calculateInsuranceCost_ValidRisks_ReturnsSum() {
        AgreementDto agreement = AgreementDto.builder()
                .agreementDateFrom(LocalDateTime.now())
                .agreementDateTo(LocalDateTime.now().plusDays(7))
                .selectedRisks(List.of(
                        AvailableRisk.MEDICAL_EXPENSES.getRiskCode(),
                        AvailableRisk.TRIP_CANCELLATION.getRiskCode()
                ))
                .build();

        doReturn(List.of(
                new RiskDto(AvailableRisk.MEDICAL_EXPENSES.getRiskCode(), new BigDecimal("50")),
                new RiskDto(AvailableRisk.TRIP_CANCELLATION.getRiskCode(), new BigDecimal("75"))
        )).when(riskPremiumCalculator).calculatePremiumForAllRisks(
                argThat(req -> req != null && req.getSelectedRisks() != null),
                any()
        );

        BigDecimal result = underwritingService.calculateInsuranceCost(agreement, new PersonDto());

        assertEquals(new BigDecimal("125"), result);
    }


    @Test
    void calculateRiskPremiums_MatchingCalculators_ReturnsCombinedResults() {
        TravelRiskPremiumCalculator mockCalculator = mock(TravelRiskPremiumCalculator.class);
        when(mockCalculator.getRiskIc()).thenReturn("TRAVEL_MEDICAL");
        when(mockCalculator.calculatePremium(any(), any())).thenReturn(new BigDecimal("100"));
        when(riskCalculators.stream()).thenReturn(Stream.of(mockCalculator));

        AgreementDto agreement = AgreementDto.builder()
                .selectedRisks(List.of("TRAVEL_MEDICAL"))
                .build();

        List<RiskDto> result = underwritingService.calculateRiskPremiums(agreement, new PersonDto());

        assertEquals(1, result.size());
        assertEquals("TRAVEL_MEDICAL", result.get(0).getRiskIc());
    }


    private AgreementDto createValidAgreement() {
        return AgreementDto.builder()
                .agreementDateFrom(LocalDateTime.now())
                .agreementDateTo(LocalDateTime.now().plusDays(7))
                .selectedRisks(List.of("TRAVEL_MEDICAL", "TRAVEL_CANCELLATION"))
                .build();
    }
}
