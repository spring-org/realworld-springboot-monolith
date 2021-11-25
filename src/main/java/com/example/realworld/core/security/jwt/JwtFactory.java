package com.example.realworld.core.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtFactory {

    private final Key key;

    public JwtFactory(@Value(value = "${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String email, int expiredTime) {
        HashMap<String, Object> claims = new HashMap<>();
        return createToken(claims, email, expiredTime);
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isValidToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException exception) {
            log.error("JWT Exception");
        }
        return false;
    }

    private String createToken(HashMap<String, Object> claims, String email, int expiredTime) {
        return Jwts.builder()
                .setHeader(settingHeaders())
                .signWith(key, SignatureAlgorithm.HS512)
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(settingsDate(0))
                .setExpiration(settingsDate(expiredTime))
                .compact();
    }

    private Date settingsDate(int plusTime) {
        return Date.from(
                LocalDateTime.now().plusMinutes(plusTime)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
        );
    }

    private Map<String, Object> settingHeaders() {
        return Map.of("typ", Header.JWT_TYPE, "alg", SignatureAlgorithm.HS512);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }
}
