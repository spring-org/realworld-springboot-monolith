package com.example.realworld.application.users.service;

import com.example.realworld.application.users.domain.User;
import com.example.realworld.application.users.dto.RequestSaveUser;
import com.example.realworld.application.users.dto.RequestUpdateUser;
import com.example.realworld.application.users.dto.ResponseProfile;
import com.example.realworld.application.users.dto.ResponseUser;
import com.example.realworld.application.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

/**
 * @SpringBootTest 대신 @ExtendWith(MockitoExtension.class) 를 사용하여 <br/>
 * IOC Container가 생성되지 않으며, 필요한 서비스 객체만 실제로 생성되어 빠른 테스트를 제공
 */
@ExtendWith(MockitoExtension.class)
class UserBusinessServiceTest {

    private UserBusinessService userBusinessService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.userBusinessService = new UserBusinessService(userRepository);
    }

    @DisplayName("사용자 등록 테스트")
    @Test
    void when_addUser_expected_success_createUser() {
        // given
        RequestSaveUser saveUser =
                RequestSaveUser.of("seokrae@gmail.com", "seok", "1234");
        User actual = RequestSaveUser.toEntity(saveUser);
        // mocking 데이터 생성
        given(userRepository.save(any())).willReturn(actual);
        given(userRepository.findByEmail(any())).willReturn(Optional.of(actual));
        // when
        ResponseUser responseUser = userBusinessService.addUser(saveUser);
        User userByEmail = userBusinessService.findUserByEmail(actual.getEmail());

        // then
        assertThat(actual.getEmail()).isEqualTo(responseUser.getEmail());
        assertThat(actual.getEmail()).isEqualTo(userByEmail.getEmail());
    }

    @DisplayName("사용자 정보 수정 테스트")
    @Test
    void when_updateUser_expect_success_change_username() {
        RequestSaveUser saveUser =
                RequestSaveUser.of("seokrae@gmail.com", "seok", "1234");
        User savedUser = RequestSaveUser.toEntity(saveUser);

        RequestUpdateUser updateUser =
                RequestUpdateUser.of("seokrae@gmail.com", "seokrae", "", "", "");

        given(userRepository.save(any())).willReturn(savedUser);
        given(userRepository.findByEmail(any())).willReturn(Optional.of(savedUser));

        ResponseUser responseUser = userBusinessService.addUser(saveUser);
        ResponseUser updatedUser = userBusinessService.updateUser(responseUser.getEmail(), updateUser);

        assertThat(updatedUser.getUserName()).isEqualTo("seokrae");
    }

    @DisplayName("특정 사용자의 프로필 조회 테스트")
    @Test
    void testCase1() {
        String email = "seokrae@gmail.com";

        RequestSaveUser saveUser =
                RequestSaveUser.of(email, "seok", "1234");
        User savedUser = RequestSaveUser.toEntity(saveUser);

        given(userRepository.findByEmail(any())).willReturn(Optional.of(savedUser));

        ResponseProfile profile = userBusinessService.getProfile(email);

        assertThat(profile.getUserName()).isEqualTo("seok");
    }
}