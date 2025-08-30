package org.nikita.core.underwriting.calcs.medical;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
class AgeCalcCoefficientMedical {

//    @IMedicalVAgeCoefficient
    public BigDecimal getAgeCoefficient(int age) {
        if (age <= 10) {
            return new BigDecimal("0.5");
        } else if (age <= 17) {
            return new BigDecimal("0.8");
        } else if (age <= 25) {
            return new BigDecimal("1.2");
        } else if (age <= 50) {
            return new BigDecimal("1.3");
        } else {
            return new BigDecimal("1.5");
        }
    }
}
