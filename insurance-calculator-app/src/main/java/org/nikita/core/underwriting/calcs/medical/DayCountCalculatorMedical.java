package org.nikita.core.underwriting.calcs.medical;

import org.nikita.api.dto.AgreementDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static java.time.Duration.between;

@Component
class DayCountCalculatorMedical {

    public BigDecimal calculateDayCount(AgreementDto request) {
        long daysBetween = between(request.getAgreementDateFrom(), request.getAgreementDateTo()).toDays();
        return BigDecimal.valueOf(daysBetween);
    }
}
