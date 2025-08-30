package org.nikita.core.service;

import org.nikita.api.dto.AgreementDto;

public interface ITravelPremiumCalculator {
    void calculatePremiums(AgreementDto agreement);

}
