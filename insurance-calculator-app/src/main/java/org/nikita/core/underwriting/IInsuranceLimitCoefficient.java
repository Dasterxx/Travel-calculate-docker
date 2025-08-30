package org.nikita.core.underwriting;

import java.math.BigDecimal;

public interface IInsuranceLimitCoefficient {
    BigDecimal getInsuranceLimitCoefficient(BigDecimal insuranceLimit);
    String getInsuranceLimitGroup(BigDecimal insuranceLimit);
}
