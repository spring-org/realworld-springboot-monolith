package com.example.realworld.application.users.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.realworld.application.users.domain.ProfileTest.createProfile;
import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    public static User createUser(Long id) {
        return User.builder()
                .id(id)
                .email("seokrae@gmail.com")
                .password("!234")
                .profile(createProfile())
                .token("")
                .build();
    }

    @DisplayName("사용자 엔티티 생성 테스트")
    @Test
    void when_createUser_expected_equals_email() {

        User user = createUser(1L);

        assertThat(user.getEmail()).isEqualTo("seokrae@gmail.com");
    }

    @DisplayName("사용자 엔티티 비교 테스트")
    @Test
    void when_saveUser_expected_isEquals() {
        // given
        User actual = createUser(1L);

        // when
        User expected = createUser(1L);

        // then
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
    }

    @DisplayName("사용자 엔티티 비교 실패 테스트")
    @Test
    void when_createUser_expected_not_equals_user() {
        // given
        User actual = createUser(1L);

        // when
        User expected = createUser(2L);

        // then
        assertThat(actual.getId()).isNotEqualTo(expected.getId());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
    }
}