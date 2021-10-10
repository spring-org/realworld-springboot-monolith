package com.example.realworld.core.jwt;

import com.example.realworld.core.security.context.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    private final Key key;

    public JwtUtils(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * @param userContext 사용자 인증 정보
     * @param expiredTime 만료일 정보
     * @return JWT 발급
     */
    public String generateToken(UserContext userContext, int expiredTime) {
        HashMap<String, Object> claims = new HashMap<>();
        return createToken(claims, userContext, expiredTime);
    }

    /**
     * 이메일 정보 반환 메서드
     *
     * @param token JWT 정보
     * @return 사용자 이메일 정보 추출 및 반환
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 토큰 Validation 체크 메서드
     *
     * @param token       JWT 정보
     * @param userContext UserContext 사용자 인증 정보
     * @return JWT 유효성 여부
     */
    public boolean isToken(String token, UserContext userContext) {
        String email = extractEmail(token);
        return email.equals(userContext.email());
    }

    private String createToken(HashMap<String, Object> claims, UserContext userContext, int expiredTime) {
        return Jwts.builder()
                .setHeader(settingHeaders())
                .signWith(key, SignatureAlgorithm.HS512)
                .setClaims(claims)
                .setSubject(userContext.email())
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
