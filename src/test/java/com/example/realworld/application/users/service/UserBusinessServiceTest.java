package com.example.realworld.application.users.service;

import com.example.realworld.application.users.dto.*;
import com.example.realworld.application.users.exception.DuplicateUserException;
import com.example.realworld.application.users.exception.NotFoundUserException;
import com.example.realworld.application.users.persistence.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.realworld.application.users.UserFixture.getRequestSaveUser;
import static com.example.realworld.application.users.UserFixture.getRequestUpdateUser;
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

    @DisplayName("신규 사용자 등록 테스트")
    @Test
    void when_addUser_expected_success_createUser() {
        // given
        RequestSaveUser saveUser = getRequestSaveUser("seokrae@gmail.com", "seok");
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
        RequestSaveUser saveUser = getRequestSaveUser(email, "seok");
        // when
        userBusinessService.postUser(saveUser);
        // then
        assertThatExceptionOfType(DuplicateUserException.class)
                .isThrownBy(() -> userBusinessService.postUser(saveUser));
    }

    @DisplayName("사용자 프로필 조회 테스트")
    @Test
    void when_getProfile_expect_success_profile_info() {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser saveUser = getRequestSaveUser(email, "seok");
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
        RequestSaveUser saveUser = getRequestSaveUser(email, "seok");
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
        RequestSaveUser saveUser = getRequestSaveUser(email, "seok");
        // when
        ResponseUser savedUser = userBusinessService.postUser(saveUser);
        ResponseUser currentUser = userBusinessService.getUserByEmail(email);
        // then
        assertThat(savedUser.getUserName()).isEqualTo(currentUser.getUserName());
    }

    @DisplayName("사용자 정보 수정 테스트")
    @Test
    void when_updateUser_expect_success_change_username() {
        // given
        String currentUserEmail = "seokrae@gmail.com";
        RequestSaveUser saveUser = getRequestSaveUser(currentUserEmail, "seok");
        RequestUpdateUser updateUser = getRequestUpdateUser(currentUserEmail, "seokrae", "12345", "/image.png", "hello bio");
        // when
        userBusinessService.postUser(saveUser);
        ResponseUser updatedUser = userBusinessService.updateUser(currentUserEmail, updateUser);
        // then
        assertThat(updatedUser.getUserName()).isEqualTo("seokrae");
    }

    @DisplayName("사용자 정보 수정 요청 시 변동 없는 테스트(수정 데이터 입력X)")
    @Test
    void when_updateUser_expect_success_no_data() {
        // given
        String currentUserEmail = "seokrae@gmail.com";
        RequestSaveUser saveUser = getRequestSaveUser(currentUserEmail, "seok");
        RequestUpdateUser updateUser = getRequestUpdateUser("", "", "", "", "");
        // when
        userBusinessService.postUser(saveUser);
        ResponseUser updatedUser = userBusinessService.updateUser(currentUserEmail, updateUser);
        // then
        assertThat(currentUserEmail).isEqualTo(updatedUser.getEmail());
    }

    @DisplayName("특정 사용자의 프로필 조회 테스트")
    @Test
    void when_getProfile_expect_success_user_profile() {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser saveUser = getRequestSaveUser(email, "seok");
        // when
        userBusinessService.postUser(saveUser);
        ResponseProfile profile = userBusinessService.getProfile(email);
        // then
        assertThat(profile.getUserName()).isEqualTo("seok");
    }

    @DisplayName("로그인 및 토큰 발급 테스트")
    @Test
    void when_login_expect_success_generate_token() {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser saveUser = getRequestSaveUser(email, "seok");
        // when
        userBusinessService.postUser(saveUser);

        RequestLoginUser requestLoginUser = RequestLoginUser.of("seokrae@gmail.com", "1234");
        ResponseUser responseUser = userBusinessService.login(requestLoginUser);

        assertThat(responseUser.getToken()).isNotEmpty();
    }
}