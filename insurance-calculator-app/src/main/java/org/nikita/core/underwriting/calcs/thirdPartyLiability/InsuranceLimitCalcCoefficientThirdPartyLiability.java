package org.nikita.core.underwriting.calcs.thirdPartyLiability;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
class InsuranceLimitCalcCoefficientThirdPartyLiability {
    public BigDecimal getInsuranceLimitCoefficient(BigDecimal insuranceLimit) {

        if (insuranceLimit.compareTo(new BigDecimal("10000")) <= 0) {
            return new BigDecimal("1.0");
        } else if (insuranceLimit.compareTo(new BigDecimal("15000")) <= 0) {
            return new BigDecimal("1.5");
        } else {
            return new BigDecimal("2.0");
        }
    }
}
