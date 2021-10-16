package com.example.realworld.core.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = "jwt.secret=123456ASDFQWASDASDAS123123123123DASDASDASDEWGD78901234567890123456789000")
class JwtFactoryTest {

    @Value(value = "${jwt.secret}")
    private String secret;
    private JwtFactory jwtFactory;

    @BeforeEach
    void setUp() {
        jwtFactory = new JwtFactory(secret);
    }

    @DisplayName("JWT 생성 테스트")
    @Test
    void when_generateToken_expect_success_json_web_token() {
        // given
        // when
        String token = jwtFactory.generateToken("seokrae@gmail.com", 0);
        // then
        assertThat(token).isNotEmpty();
    }

    @DisplayName("토큰 정보 내에 사용자 이메일 정보 추출 테스트")
    @Test
    void when_extractEmail_expect_success_extract_user_email() {
        // given
        String token = jwtFactory.generateToken("seokrae@gmail.com", 1);
        // when
        String extractEmail = jwtFactory.extractEmail(token);
        // then
        assertThat(extractEmail).isEqualTo("seokrae@gmail.com");
    }

    @DisplayName("토큰 정보 유효성 검사 테스트")
    @Test
    void when_isToken_expect_success_validation_token() {
        // given
        String token = jwtFactory.generateToken("seokrae@gmail.com", 1);
        // when
        boolean isToken = jwtFactory.isValidToken(token);
        // then
        assertThat(isToken).isTrue();
    }


    @DisplayName("토큰 정보 유효성 검사 실패 (토큰 만료 예외) 테스트")
    @Test
    void when_isToken_expect_fail_expired_token_exception() {
        // given
        String token = jwtFactory.generateToken("seokrae@gmail.com", 0);
        // when
        boolean isValidToken = jwtFactory.isValidToken(token);
        // then
        assertThat(isValidToken).isFalse();
    }
}