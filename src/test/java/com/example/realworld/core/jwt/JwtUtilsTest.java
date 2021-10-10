package com.example.realworld.core.jwt;

import com.example.realworld.core.security.context.UserContext;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.example.realworld.application.users.UserFixture.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@TestPropertySource(properties = "jwt.secret=123456ASDFQWASDASDAS123123123123DASDASDASDEWGD78901234567890123456789000")
class JwtUtilsTest {

    @Value(value = "${jwt.secret}")
    private String secret;
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils(secret);
    }

    @DisplayName("JWT 생성 테스트")
    @Test
    void when_generateToken_expect_success_json_web_token() {
        // given
        UserContext userContext = UserContext.of(createUser("seokrae@gmail.com"));
        // when
        String token = jwtUtils.generateToken(userContext, 0);
        // then
        assertThat(token).isNotEmpty();
    }

    @DisplayName("토큰 정보 내에 사용자 이메일 정보 추출 테스트")
    @Test
    void when_extractEmail_expect_success_extract_user_email() {
        // given
        UserContext userContext = UserContext.of(createUser("seokrae@gmail.com"));
        String token = jwtUtils.generateToken(userContext, 1);
        // when
        String extractEmail = jwtUtils.extractEmail(token);
        // then
        assertThat(extractEmail).isEqualTo(userContext.email());
    }

    @DisplayName("토큰 정보 유효성 검사 테스트")
    @Test
    void when_isToken_expect_success_validation_token() {
        // given
        UserContext userContext = UserContext.of(createUser("seokrae@gmail.com"));
        String token = jwtUtils.generateToken(userContext, 1);
        // when
        boolean isToken = jwtUtils.isToken(token, userContext);
        // then
        assertThat(isToken).isTrue();
    }

    @DisplayName("토큰 정보 유효성 검사 실패 (이메일 정보 매칭 실패) 테스트")
    @Test
    void when_isToken_expect_fail_not_equals_email() {
        // given
        UserContext userContext = UserContext.of(createUser("seokrae@gmail.com"));
        String token = jwtUtils.generateToken(userContext, 1);
        // when
        UserContext otherUserContext = UserContext.of(createUser("other@gmail.com"));
        boolean isToken = jwtUtils.isToken(token, otherUserContext);
        // then
        assertThat(isToken).isFalse();
    }

    @DisplayName("토큰 정보 유효성 검사 실패 (토큰 만료 예외) 테스트")
    @Test
    void when_isToken_expect_fail_expired_token_exception() {
        // given
        UserContext userContext = UserContext.of(createUser("seokrae@gmail.com"));
        // when
        String token = jwtUtils.generateToken(userContext, 0);
        // then
        assertThatExceptionOfType(ExpiredJwtException.class)
                .isThrownBy(() -> jwtUtils.isToken(token, userContext));
    }
}