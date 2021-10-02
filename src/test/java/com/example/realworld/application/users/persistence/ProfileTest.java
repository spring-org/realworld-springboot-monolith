package com.example.realworld.application.users.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileTest {

    public static Profile createProfile() {
        return Profile.of("seokrae", "bio hello", null, false);
    }

    @DisplayName("프로필 엔티티 생성 테스트")
    @Test
    void when_createProfile_expected_equals_username() {
        Profile profile = createProfile();

        assertThat(profile.userName()).isEqualTo("seokrae");
    }
}