package org.nikita.core.service;

import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreResult;
import org.nikita.api.dto.AgreementDto;

public interface TravelCalculatePremiumService {

    TravelCalculatePremiumCoreResult calculatePremium(TravelCalculatePremiumCoreCommand command);
    TravelCalculatePremiumCoreResult calculatePremium(AgreementDto command);

}
