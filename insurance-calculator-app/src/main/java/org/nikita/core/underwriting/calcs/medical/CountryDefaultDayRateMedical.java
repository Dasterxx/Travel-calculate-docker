package org.nikita.core.underwriting.calcs.medical;

import lombok.RequiredArgsConstructor;
import org.nikita.api.dto.AgreementDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
class CountryDefaultDayRateMedical {
    public BigDecimal calculateCountryDefaultDayRate(AgreementDto request) {
        return BigDecimal.valueOf(10);
    }
}
