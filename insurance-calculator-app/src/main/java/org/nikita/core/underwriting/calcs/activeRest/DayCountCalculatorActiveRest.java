package org.nikita.core.underwriting.calcs.activeRest;

import org.nikita.api.dto.AgreementDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static java.time.Duration.between;

@Component
class DayCountCalculatorActiveRest {
    public BigDecimal calculateDayCount(AgreementDto request) {
        long days = between(request.getAgreementDateFrom(), request.getAgreementDateTo()).toDays();
        return BigDecimal.valueOf(days > 0 ? days : 1);
    }
}
