package com.example.realworld.application.users.repository;

import com.example.realworld.application.users.exception.NotFoundUserException;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.application.users.persistence.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private User savedUser;

    private User createUser(String email) {
        return User.of(email, "1234", "seokrae");
    }

    @BeforeEach
    void setUp() {
        savedUser = userRepository.save(createUser("seokrae@gmail.com"));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @DisplayName("사용자 등록 확인 테스트")
    @Test
    void when_createUser_expected_exist() {
        // when
        User findUser = userRepository.findByEmail("seokrae@gmail.com")
                .orElseThrow(() -> new NotFoundUserException("사용자 정보가 존재하지 않습니다."));

        // then
        assertThat(findUser.getEmail()).isEqualTo("seokrae@gmail.com");
    }
}