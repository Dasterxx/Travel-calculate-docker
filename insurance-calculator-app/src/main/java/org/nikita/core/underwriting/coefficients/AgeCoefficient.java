package org.nikita.core.underwriting.coefficients;

import lombok.extern.slf4j.Slf4j;
import org.nikita.core.config.DataSourceConfig;
import org.nikita.core.underwriting.IAgeCoefficient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.*;

@Component
@Slf4j
class AgeCoefficient implements IAgeCoefficient {
    private final DataSourceConfig dataSourceConfig;

    public AgeCoefficient(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
    }


    @Override
    public BigDecimal getAgeCoefficient(Integer age) {
        if (age == null) {
            log.error("Age is null");
            return BigDecimal.ONE;
        }

        try (Connection conn = DriverManager.getConnection(
                dataSourceConfig.getUrl(),
                dataSourceConfig.getUsername(),
                dataSourceConfig.getPassword()
        )) {
            String query = "SELECT age_coefficient FROM age_coefficients WHERE age_group = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                String ageGroup = getAgeGroup(age);
                stmt.setString(1, ageGroup);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getBigDecimal("age_coefficient");
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Error fetching age coefficient", e);
        }

        return BigDecimal.ONE;
    }

    @Override
    public String getAgeGroup(Integer age) {
        if (age <= 17) {
            return "1-17";
        } else if (age <= 25) {
            return "18-25";
        } else if (age <= 50) {
            return "26-50";
        } else {
            return "51+";
        }
    }
}
