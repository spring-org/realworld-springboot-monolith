package com.example.realworld.application.users.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileTest {

    public static Profile createProfile() {
        return Profile.builder()
                .bio("hello")
                .userName("seokrae")
                .image(null)
                .build();
    }

    @DisplayName("프로필 엔티티 생성 테스트")
    @Test
    void when_createProfile_expected_equals_username() {
        Profile profile = createProfile();

        assertThat(profile.getUserName()).isEqualTo("seokrae");
    }
}