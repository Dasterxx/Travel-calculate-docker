package org.nikita.core.underwriting;

import java.math.BigDecimal;

public interface IAgeCoefficient {
    BigDecimal getAgeCoefficient(Integer age);
    String getAgeGroup(Integer age);
}
