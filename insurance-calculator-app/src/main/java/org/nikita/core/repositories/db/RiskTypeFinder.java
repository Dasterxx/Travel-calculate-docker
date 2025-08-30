package org.nikita.core.repositories.db;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Component
class RiskTypeFinder {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/internship";
        String user = "postgres";
        String password = "pass";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT * FROM pg_enum WHERE enumtypid = 'risk_type'::regtype";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        System.out.println(rs.getString("enumlabel"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
