package org.nikita.rest.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreResult;
import org.nikita.core.service.TravelCalculatePremiumService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/insurance/travel/api/v1")
@RequiredArgsConstructor
@Validated
public class TravelCalculatePremiumControllerV1 {

    private final TravelCalculatePremiumRequestLogger requestLogger;
    private final TravelCalculatePremiumResponseLogger responseLogger;
    private final TravelCalculatePremiumService calculatePremiumService;


    @PostMapping(path = "/",
            consumes = "application/json",
            produces = "application/json")
    public TravelCalculatePremiumCoreResult calculatePremium(@Valid @RequestBody TravelCalculatePremiumCoreCommand request) {
        requestLogger.log(request);

        TravelCalculatePremiumCoreResult response = calculatePremiumService.calculatePremium(request);
        responseLogger.log(response);

        if (response.hasErrors()) {
            return new TravelCalculatePremiumCoreResult(response.getErrors());
        } else {
            return response;
        }
    }

}
