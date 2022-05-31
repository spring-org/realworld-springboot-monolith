package com.example.realworld.application.users.service;

import com.example.realworld.application.users.exception.NotFoundUserException;
import com.example.realworld.application.users.persistence.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.realworld.application.users.UserFixture.createUser;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDomainServiceTest {

    @InjectMocks
    private UserDomainService userDomainService;

    @Mock
    private UserRepository userRepository;

    @DisplayName("사용자 조회 테스트")
    @Test
    void when_findUserByEmail_expect_success_find_user() {
        // given
        String email = "seokrae@gmail.com";
        given(userRepository.findByEmail(any())).willReturn(Optional.of(createUser(email)));
        // when
        userDomainService.findUserByEmail(email);
        // then
        then(userRepository).should(times(1)).findByEmail(any());
    }

    @DisplayName("사용자 조회 예외 테스트")
    @Test
    void when_findUserByEmail_expect_fail_not_found_exception() {
        // given
        String email = "seokrae@gmail.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        // when
        // then
        assertThatExceptionOfType(NotFoundUserException.class)
                .isThrownBy(() -> userDomainService.findUserByEmail(email));
    }

    @DisplayName("사용자 존재여부 확인 테스트")
    @Test
    void when_existsByEmail_expect_success_true() {
        // given
        String email = "seokrae@gmail.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);
        // when
        userDomainService.existsByEmail(email);
        // then
        then(userRepository).should(times(1)).existsByEmail(email);
    }
}