package org.nikita.core.underwriting.coefficients;

import lombok.extern.slf4j.Slf4j;
import org.nikita.core.config.DataSourceConfig;
import org.nikita.core.underwriting.IInsuranceLimitCoefficient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.*;

@Component
@Slf4j
class InsuranceLimitCoefficient implements IInsuranceLimitCoefficient {
    private final DataSourceConfig dataSourceConfig;

    public InsuranceLimitCoefficient(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
    }

    @Override
    public BigDecimal getInsuranceLimitCoefficient(BigDecimal insuranceLimit) {
        if (insuranceLimit == null) {
            log.error("Insurance limit is null");
            return BigDecimal.ONE;
        }
        try (Connection conn = DriverManager.getConnection(
                dataSourceConfig.getUrl(),
                dataSourceConfig.getUsername(),
                dataSourceConfig.getPassword()
        )) {
            String query =
                    "SELECT insurance_limit_coefficient FROM insurance_limit_coefficients WHERE insurance_limit_group = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                String insuranceLimitGroup = getInsuranceLimitGroup(insuranceLimit);
                stmt.setString(1, insuranceLimitGroup);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getBigDecimal("insurance_limit_coefficient");
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Error fetching insurance limit coefficient", e);
        }
        return BigDecimal.ONE;
    }

    @Override
    public String getInsuranceLimitGroup(BigDecimal insuranceLimit) {
        if (insuranceLimit.compareTo(BigDecimal.valueOf(10000)) <= 0) {
            return "low";
        } else if (insuranceLimit.compareTo(BigDecimal.valueOf(15000)) <= 0) {
            return "medium";
        } else {
            return "high";
        }
    }
}