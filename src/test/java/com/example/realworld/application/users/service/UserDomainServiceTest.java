package com.example.realworld.application.users.service;

import com.example.realworld.application.users.exception.NotFoundUserException;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.application.users.persistence.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.realworld.application.users.UserFixture.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
class UserDomainServiceTest {
    @Autowired
    private UserDomainService userDomainService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @DisplayName("사용자 조회 테스트")
    @Test
    void when_findUserByEmail_expect_success_find_user() {
        // given
        String email = "seokrae@gmail.com";
        User actual = userDomainService.save(createUser(email));
        // when
        User expect = userDomainService.findUserByEmail(email);
        // then
        assertThat(actual).isEqualTo(expect);
    }

    @DisplayName("사용자 조회 예외 테스트")
    @Test
    void when_findUserByEmail_expect_fail_not_found_exception() {
        // given
        String email = "seokrae@gmail.com";
        userDomainService.save(createUser(email));
        // when
        String notExistsUserEmail = "not_found@gmail.com";
        // then
        assertThatExceptionOfType(NotFoundUserException.class)
                .isThrownBy(() -> userDomainService.findUserByEmail(notExistsUserEmail));
    }

    @DisplayName("사용자 존재여부 확인 테스트")
    @Test
    void when_existsByEmail_expect_success_true() {
        // given
        String email = "seokrae@gmail.com";
        userDomainService.save(createUser(email));
        // when
        boolean expect = userDomainService.existsByEmail(email);
        // then
        assertThat(expect).isTrue();
    }
}