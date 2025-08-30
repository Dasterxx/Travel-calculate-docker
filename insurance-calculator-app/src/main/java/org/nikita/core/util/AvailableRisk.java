package org.nikita.core.util;

import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum AvailableRisk {
    MEDICAL_EXPENSES("TRAVEL_MEDICAL"),
    TRIP_CANCELLATION("TRAVEL_CANCELLATION"),
    BAGGAGE_LOSS("TRAVEL_LOSS_BAGGAGE"),
    EVACUATION_REPATRIATION("TRAVEL_EVACUATION"),
    THIRD_PARTY_LIABILITY("TRAVEL_THIRD_PARTY_LIABILITY"),
    ACTIVE_REST("TRAVEL_SPORT_ACTIVITIES");

    private final String riskCode;

    private static final Set<String> VALID_RISK_CODES = Stream.of(values())
            .map(AvailableRisk::getRiskCode)
            .collect(Collectors.toSet());

    AvailableRisk(String riskCode) {
        this.riskCode = riskCode;
    }

    public static boolean isValidRisk(String riskCode) {
        return VALID_RISK_CODES.contains(riskCode);
    }
}
