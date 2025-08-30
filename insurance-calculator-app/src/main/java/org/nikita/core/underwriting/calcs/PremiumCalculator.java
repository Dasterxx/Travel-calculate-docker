//package org.nikita.core.underwriting.calcs;
//
//import lombok.extern.slf4j.Slf4j;
//import org.nikita.core.config.DataSourceConfig;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//@Component
//@Slf4j
//class PremiumCalculator {
//
//    private final DataSourceConfig dataSourceConfig;
//
//    public PremiumCalculator(DataSourceConfig dataSourceConfig) {
//        this.dataSourceConfig = dataSourceConfig;
//    }
//
//    public BigDecimal calculatePremium(Integer countryId, Integer dayCount) {
//        if (countryId == null || dayCount == null) {
//            return BigDecimal.ZERO;
//        }
//
//        try (Connection conn = DriverManager.getConnection(
//                dataSourceConfig.getUrl(),
//                dataSourceConfig.getUsername(),
//                dataSourceConfig.getPassword()
//        )) {
//            String query = "SELECT default_day_premium FROM country_default_day_premiums WHERE country_id = ?";
//            try (PreparedStatement stmt = conn.prepareStatement(query)) {
//                stmt.setInt(1, countryId);
//                try (ResultSet rs = stmt.executeQuery()) {
//                    if (rs.next()) {
//                        BigDecimal dayPremium = rs.getBigDecimal("default_day_premium");
//                        return dayPremium.multiply(BigDecimal.valueOf(dayCount));
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            log.error("Error calculating premium", e);
//        }
//        return BigDecimal.ZERO;
//    }
//}