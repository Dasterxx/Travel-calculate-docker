package org.nikita.core.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.nikita.core.security.user.UPCUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

/**
 * Utility class for generating and validating JWT tokens.
 */
@Component
@Slf4j
public class JwtUtils {

    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;
    @Value("${auth.token.expirationInMils}")
    private int jwtExpirationMs;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
            // Проверяем длину ключа (минимум 256 бит = 32 байта для HS256)
            if (keyBytes.length < 32) {
                throw new IllegalArgumentException("JWT secret key is too short. It must be at least 256 bits (32 bytes).");
            }
            secretKey = Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid JWT secret key format or length: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to decode JWT secret key from Base64", e);
        }
    }


    /**
     * Generates a JWT token for the given user.
     *
     * @param authentication the user's authentication object
     * @return the generated JWT token
     */
    public String generateTokenForUser(Authentication authentication) {
        UPCUserDetails userPrincipal = (UPCUserDetails) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .claim("id", userPrincipal.getId())
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Returns the key used for signing the JWT tokens.
     *
     * @return the key
     */
    private SecretKey getSigningKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
//        return Keys.hmacShaKeyFor(keyBytes);
        return secretKey;
    }

    /**
     * Returns the username from the given JWT token.
     *
     * @param token the JWT token
     * @return the username
     */
    public String getUserNameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    /**
     * Validates the given JWT token.
     *
     * @param token the JWT token
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parse(token);
            return true;
        } catch (ExpiredJwtException e) {
            // Логика для обработки истекшего токена
            log.error("Token is expired: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            // Логика для обработки некорректного токена
            log.error("Token is malformed: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            // Логика для обработки пустого токена
            log.error("Token is empty: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            // Логика для обработки неподдерживаемого токена
            log.error("Token is unsupported: {}", e.getMessage());
        }
        return false;
    }
}
