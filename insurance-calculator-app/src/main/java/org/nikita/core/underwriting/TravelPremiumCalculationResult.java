package org.nikita.core.underwriting;

import org.nikita.api.dto.RiskDto;

import java.math.BigDecimal;
import java.util.List;

public record TravelPremiumCalculationResult(
        BigDecimal totalPremium,
        List<RiskDto> risks
) {}
