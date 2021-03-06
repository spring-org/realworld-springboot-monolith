package com.example.realworld.application.users.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.realworld.application.users.UserFixture.createUser;
import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @DisplayName("사용자 엔티티 생성 테스트")
    @Test
    void when_createUser_expected_equals_email() {

        User user = createUser("seokrae@gmail.com");

        assertThat(user.getEmail()).isEqualTo("seokrae@gmail.com");
    }

    @DisplayName("사용자 엔티티 비교 테스트")
    @Test
    void when_saveUser_expected_isEquals() {
        // given
        User actual = createUser("seokrae@gmail.com");

        // when
        User expected = createUser("seokrae@gmail.com");

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("사용자 엔티티 비교 실패 테스트")
    @Test
    void when_createUser_expected_not_equals_user() {
        // given
        User actual = createUser("seokrae@gmail.com");

        // when
        User expected = createUser("seok@gmail.com");

        // then
        assertThat(actual).isNotEqualTo(expected);
    }
}