package org.nikita.core.underwriting;

import org.junit.jupiter.api.Nested;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@Nested
@SpringBootTest
@TestPropertySource(properties = {
        "medical.risk.limit.level.enabled=false",
        "age.coefficient.level.enabled=false"
})
class TravelUnderwritingIntegrationDisableTest {
    @Autowired
    private TravelPremiumUnderwritingImp underwritingService;

    @MockBean
    private IDateTimeService dateTimeService;

    @MockBean
    private List<TravelRiskPremiumCalculator> riskCalculators;

    @SpyBean
    private SelectedRisksPremiumCalculator riskPremiumCalculator;

    @Test
    void calculateInsuranceCost_WithDisabledFeatures_BehaviorChanges() {
        AgreementDto agreement = createValidAgreement();

        doReturn(List.of(
                new RiskDto(AvailableRisk.MEDICAL_EXPENSES.getRiskCode(), new BigDecimal("30")),
                new RiskDto(AvailableRisk.TRIP_CANCELLATION.getRiskCode(), new BigDecimal("45"))
        )).when(riskPremiumCalculator).calculatePremiumForAllRisks(
                argThat(req -> req != null && req.getSelectedRisks() != null),
                any()
        );

        BigDecimal result = underwritingService.calculateInsuranceCost(agreement, new PersonDto());

        assertEquals(new BigDecimal("75"), result,
                "Disabled features should return reduced premiums");
        verifyNoMoreInteractions(riskCalculators);
    }


    private AgreementDto createValidAgreement() {
        return AgreementDto.builder()
                .agreementDateFrom(LocalDateTime.now())
                .agreementDateTo(LocalDateTime.now().plusDays(7))
                .selectedRisks(List.of("TRAVEL_MEDICAL", "TRAVEL_CANCELLATION"))
                .build();
    }
}

