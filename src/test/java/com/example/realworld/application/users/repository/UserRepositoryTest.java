package com.example.realworld.application.users.repository;

import com.example.realworld.application.users.domain.User;
import com.example.realworld.application.users.exception.UserNotFoundException;
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

    private User createUser(Long id, String email) {
        return User.registeredUser(id, email, "1234");
    }

    @BeforeEach
    void setUp() {
        savedUser = userRepository.save(createUser(1L, "seokrae@gmail.com"));
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
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 존재하지 않습니다."));

        // then
        assertThat(findUser.getEmail()).isEqualTo("seokrae@gmail.com");
    }

    @DisplayName("사용자 수정 테스트")
    @Test
    void when_updateUser_expected_changeInfo() {
        // when
        savedUser.updateUser("update@gmail.com", "4321", "username", "bio", "gmail.com/image.png");
        userRepository.flush();

        User findUser = userRepository.findByEmail(savedUser.getEmail())
                .orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

        // then
        assertThat(findUser.getProfile().getImage()).isNotNull();
    }

    @DisplayName("사용자 수정 요청 데이터 없는 경우의 테스트")
    @Test
    void when_updatedUser_expected_noData_changeInfo() {
        // when
        savedUser.updateUser(null, null, null, null, null);
        userRepository.flush();

        User findUser = userRepository.findByEmail(savedUser.getEmail())
                .orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

        // then
        assertThat(findUser.getProfile().getImage()).isNull();
    }
}