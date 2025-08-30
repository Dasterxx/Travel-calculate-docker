package org.nikita.core.underwriting.calcs.tripCancellation;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TravelCostCoefficient {
    public BigDecimal calculateCoefficient(BigDecimal travelCost) {
        if (travelCost.compareTo(BigDecimal.valueOf(20000)) >= 0) {
            return BigDecimal.valueOf(500);
        } else if (travelCost.compareTo(BigDecimal.valueOf(10000)) >= 0) {
            return BigDecimal.valueOf(100);
        } else if (travelCost.compareTo(BigDecimal.valueOf(5000)) >= 0) {
            return BigDecimal.valueOf(30);
        } else {
            return BigDecimal.valueOf(10);
        }
    }
}
