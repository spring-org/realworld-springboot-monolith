package com.example.realworld.application.users.service;

import com.example.realworld.application.users.dto.RequestSaveUser;
import com.example.realworld.application.users.dto.RequestUpdateUser;
import com.example.realworld.application.users.dto.ResponseProfile;
import com.example.realworld.application.users.dto.ResponseUser;
import com.example.realworld.application.users.exception.DuplicateUserException;
import com.example.realworld.application.users.exception.NotFoundUserException;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.application.users.persistence.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
class UserBusinessServiceTest {
    @Autowired
    private UserBusinessService userBusinessService;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @DisplayName("사용자 등록 테스트")
    @Test
    void when_addUser_expected_success_createUser() {
        // given
        RequestSaveUser saveUser =
                RequestSaveUser.of("seokrae@gmail.com", "seok", "1234");
        // when
        ResponseUser responseUser = userBusinessService.postUser(saveUser);
        // then
        assertThat(saveUser.getEmail()).isEqualTo(responseUser.getEmail());
    }

    @DisplayName("중복된 사용자 등록 테스트")
    @Test
    void when_duplicateAddUser_expect_fail_exception() {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser saveUser =
                RequestSaveUser.of(email, "seok", "1234");
        User actual = RequestSaveUser.toEntity(saveUser);
        // when
        userRepository.save(actual);
        // then
        assertThatExceptionOfType(DuplicateUserException.class)
                .isThrownBy(() -> userBusinessService.postUser(saveUser));
    }

    @DisplayName("사용자 프로필 조회 테스트")
    @Test
    void when_getProfile_expect_success_profile_info() {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser saveUser =
                RequestSaveUser.of(email, "seok", "1234");
        // when
        ResponseUser responseUser = userBusinessService.postUser(saveUser);
        ResponseProfile profile = userBusinessService.getProfile(email);
        // then
        assertThat(responseUser.getUserName()).isEqualTo(profile.getUserName());
    }

    @DisplayName("사용자 프로필 조회 실패 테스트")
    @Test
    void when_getProfile_expect_fail_profile_info() {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser saveUser =
                RequestSaveUser.of(email, "seok", "1234");
        // when
        userBusinessService.postUser(saveUser);
        // then
        assertThatExceptionOfType(NotFoundUserException.class)
                .isThrownBy(() -> userBusinessService.getProfile("fail@gmail.com"));
    }

    @DisplayName("현재 사용자 조회 테스트")
    @Test
    void when_getUserByEmail_expect_success_currentUser() {
        String email = "seokrae@gmail.com";
        RequestSaveUser saveUser =
                RequestSaveUser.of(email, "seok", "1234");
        // when
        ResponseUser savedUser = userBusinessService.postUser(saveUser);
        ResponseUser currentUser = userBusinessService.getUserByEmail(email);
        // then
        assertThat(savedUser.getUserName()).isEqualTo(currentUser.getUserName());
    }

    @DisplayName("사용자 정보 수정 테스트")
    @Test
    void when_updateUser_expect_success_change_username() {
        // givien
        String email = "seokrae@gmail.com";
        RequestSaveUser saveUser =
                RequestSaveUser.of(email, "seok", "1234");
        RequestUpdateUser updateUser =
                RequestUpdateUser.of(email, "seokrae", "12345", "/image.png", "hello bio");
        // when
        userBusinessService.postUser(saveUser);
        ResponseUser updatedUser = userBusinessService.updateUser(email, updateUser);
        // then
        assertThat(updatedUser.getUserName()).isEqualTo("seokrae");
    }

    @DisplayName("사용자 정보 수정 요청 시 변동 없는 테스트(수정 데이터 입력X)")
    @Test
    void when_updateUser_expect_success_no_data() {
        RequestSaveUser saveUser =
                RequestSaveUser.of("seokrae@gmail.com", "seok", "1234");
        RequestUpdateUser updateUser =
                RequestUpdateUser.of("", "", "", "", "");

        ResponseUser responseUser = userBusinessService.postUser(saveUser);
        ResponseUser updatedUser = userBusinessService.updateUser(responseUser.getEmail(), updateUser);

        assertThat(updatedUser.getEmail()).isEqualTo(responseUser.getEmail());
    }

    @DisplayName("특정 사용자의 프로필 조회 테스트")
    @Test
    void when_getProfile_expect_success_user_profile() {
        String email = "seokrae@gmail.com";

        RequestSaveUser saveUser =
                RequestSaveUser.of(email, "seok", "1234");

        userBusinessService.postUser(saveUser);
        ResponseProfile profile = userBusinessService.getProfile(email);

        assertThat(profile.getUserName()).isEqualTo("seok");
    }

    @DisplayName("사용자가 존재하는지 확인하는 테스트")
    @Test
    void when_existsUserByEmail_expect_success_true() {
        String email = "seokrae@gmail.com";

        RequestSaveUser saveUser =
                RequestSaveUser.of(email, "seok", "1234");

        userBusinessService.postUser(saveUser);
        boolean exists = userBusinessService.existsUserByEmail(email);

        assertThat(exists).isTrue();
    }
}