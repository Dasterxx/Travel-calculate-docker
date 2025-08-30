//
//package org.nikita.core.underwriting.calcs;
//
//import lombok.extern.slf4j.Slf4j;
//import org.nikita.core.underwriting.IAgeCoefficient;
//import org.nikita.core.underwriting.IInsuranceLimitCoefficient;
//import org.nikita.core.validations.ValidationErrorFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//
//import static java.math.RoundingMode.HALF_UP;
//
//@Component
//@Slf4j
//class FullPremiumCalculator {
//
//    private final PremiumCalculator calculatePremium;
//    private final IAgeCoefficient ageCoefficient;
//    private final IInsuranceLimitCoefficient insuranceLimitCoefficient;
//    private final ValidationErrorFactory errorCodeProperties;
//
//    @Autowired
//    public FullPremiumCalculator(PremiumCalculator calculatePremium,
//                                 IAgeCoefficient ageCoefficient,
//                                 IInsuranceLimitCoefficient insuranceLimitCoefficient, ValidationErrorFactory errorCodeProperties) {
//        this.calculatePremium = calculatePremium;
//        this.ageCoefficient = ageCoefficient;
//        this.insuranceLimitCoefficient = insuranceLimitCoefficient;
//        this.errorCodeProperties = errorCodeProperties;
//    }
//
//    public BigDecimal calculateFullPremium(Integer countryId,
//                                           Integer dayCount,
//                                           Integer age,
//                                           BigDecimal insuranceLimit) {
//        if (countryId == null || dayCount == null || age == null || insuranceLimit == null) {
//            log.error("One or more input parameters are null");
//            throw new IllegalArgumentException(errorCodeProperties.getErrorDescription("ERROR_C0DE_9"));
//        }
//
//        BigDecimal premium = calculatePremium.calculatePremium(countryId, dayCount);
//        log.info("Base premium: {}", premium);
//
//        if (premium.compareTo(BigDecimal.ZERO) == 0) {
//            log.error("Base premium is zero");
//            throw new RuntimeException(errorCodeProperties.getErrorDescription("ERROR_CODE_10"));
//        }
//
//        BigDecimal ageCoefficientValue = ageCoefficient.getAgeCoefficient(age);
//        log.info("Age coefficient: {}", ageCoefficientValue);
//
//        BigDecimal insuranceLimitCoefficientValue = insuranceLimitCoefficient.getInsuranceLimitCoefficient(insuranceLimit);
//        log.info("Insurance limit coefficient: {}", insuranceLimitCoefficientValue);
//
//        return premium.multiply(ageCoefficientValue).multiply(insuranceLimitCoefficientValue)
//                .setScale(2, HALF_UP);
//    }
//}