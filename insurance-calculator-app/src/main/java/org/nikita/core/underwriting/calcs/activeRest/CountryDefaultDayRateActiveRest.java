package org.nikita.core.underwriting.calcs.activeRest;

import lombok.RequiredArgsConstructor;
import org.nikita.api.dto.AgreementDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
class CountryDefaultDayRateActiveRest {
    public BigDecimal calculateCountryDefaultDayRate(AgreementDto request) {
        return BigDecimal.valueOf(10);
    }
}
