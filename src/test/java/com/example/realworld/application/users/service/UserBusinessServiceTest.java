package com.example.realworld.application.users.service;

import com.example.realworld.application.users.dto.*;
import com.example.realworld.application.users.exception.DuplicateUserException;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.application.users.persistence.repository.UserRepository;
import com.example.realworld.core.security.jwt.JwtFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.realworld.application.users.UserFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserBusinessServiceTest {

    @InjectMocks
    private UserBusinessService userBusinessService;
    @Mock
    private UserDomainService userDomainService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtFactory jwtFactory;
    @Spy
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @DisplayName("신규 사용자 등록 테스트")
    @Test
    void when_addUser_expected_success_createUser() {
        // given
        RequestSaveUser saveUser = getRequestSaveUser("seokrae@gmail.com", "seok");
        User user = RequestSaveUser.toEntity(saveUser, passwordEncoder);

        given(userDomainService.existsByEmail(saveUser.getEmail())).willReturn(false);
        given(userDomainService.save(user)).willReturn(user);
        // when
        userBusinessService.postUser(saveUser);
        // then
        then(userDomainService).should(times(1)).existsByEmail(saveUser.getEmail());
        then(userDomainService).should(times(1)).save(user);
    }

    @DisplayName("중복된 사용자 등록 예외 테스트")
    @Test
    void when_duplicateAddUser_expect_fail_exception() {
        // given
        String email = "seokrae@gmail.com";
        RequestSaveUser saveUser = getRequestSaveUser(email, "seok");
        // when
        when(userDomainService.existsByEmail(email)).thenReturn(true);
        // then
        assertThatExceptionOfType(DuplicateUserException.class)
                .isThrownBy(() -> userBusinessService.postUser(saveUser));
    }

    @DisplayName("사용자 프로필 조회 테스트")
    @Test
    void when_getProfile_expect_success_profile_info() {
        // given
        String email = "seokrae@gmail.com";
        given(userDomainService.findUserByEmail(email)).willReturn(createUser(email));
        // when
        userBusinessService.getProfile(email);
        // then
        then(userDomainService).should(times(1)).findUserByEmail(email);
    }

    @DisplayName("현재 사용자 조회 테스트")
    @Test
    void when_getUserByEmail_expect_success_currentUser() {
        String email = "seokrae@gmail.com";
        given(userDomainService.findUserByEmail(email)).willReturn(createUser(email));
        // when
        ResponseUser userByEmail = userBusinessService.getUserByEmail(email);
        // then
        then(userDomainService).should(times(1)).findUserByEmail(email);
        assertThat(userByEmail.getUserName()).isEqualTo("seokrae");
    }

    @DisplayName("사용자 정보 수정 테스트")
    @Test
    void when_updateUser_expect_success_change_username() {
        // given
        String email = "seokrae@gmail.com";
        RequestUpdateUser updateUser = getRequestUpdateUser("seokrae", "12345", "/image.png", "hello bio");
        // when
        given(userDomainService.findUserByEmail(email)).willReturn(createUser(email));

        ResponseUser responseUser = userBusinessService.updateUser(email, updateUser);
        // then
        then(userDomainService).should(times(1)).findUserByEmail(email);
        assertThat(responseUser.getImage()).isEqualTo("/image.png");
        assertThat(responseUser.getBio()).isEqualTo("hello bio");
    }

    @DisplayName("사용자 정보 수정 요청 시 변동 없는 테스트(수정 데이터 입력X)")
    @Test
    void when_updateUser_expect_success_no_data() {
        // given
        String email = "seokrae@gmail.com";
        RequestUpdateUser reqUpdateUser = getRequestUpdateUser("", "", "", "");
        given(userDomainService.findUserByEmail(email)).willReturn(createUser(email));
        // when

        ResponseUser updatedUser = userBusinessService.updateUser(email, reqUpdateUser);
        // then
        then(userDomainService).should(times(1)).findUserByEmail(email);
        assertThat(updatedUser.getUserName()).isEqualTo("seokrae");
        assertThat(updatedUser.getBio()).isNull();
        assertThat(updatedUser.getImage()).isNull();
    }

    @DisplayName("특정 사용자의 프로필 조회 테스트")
    @Test
    void when_getProfile_expect_success_user_profile() {
        // given
        String email = "seokrae@gmail.com";
        given(userDomainService.findUserByEmail(email)).willReturn(createUser(email));
        // when
        ResponseProfile profile = userBusinessService.getProfile(email);
        // then
        then(userDomainService).should(times(1)).findUserByEmail(email);
        assertThat(profile.getUserName()).isEqualTo("seokrae");
    }

    @DisplayName("로그인 및 토큰 발급 테스트")
    @Test
    void when_login_expect_success_generate_token() {
        // given
        String email = "seokrae@gmail.com";
        // when
        given(userDomainService.findUserByEmail(email)).willReturn(createUser(email));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        // then
        ResponseUser responseUser = userBusinessService.login(RequestLoginUser.of(email, "1234"));

        then(jwtFactory).should(times(1)).generateToken(email, 1);

    }
}