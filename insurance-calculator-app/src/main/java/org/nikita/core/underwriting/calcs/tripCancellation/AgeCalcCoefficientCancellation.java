package org.nikita.core.underwriting.calcs.tripCancellation;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
class AgeCalcCoefficientCancellation {
    public BigDecimal calculateAgeCoefficient(int age) {
        if (age <= 9) {
            return BigDecimal.valueOf(5);
        } else if (age <= 17) {
            return BigDecimal.valueOf(10);
        } else if (age <= 39) {
            return BigDecimal.valueOf(20);
        } else if (age <= 64) {
            return BigDecimal.valueOf(30);
        } else {
            return BigDecimal.valueOf(50);
        }
    }
}
