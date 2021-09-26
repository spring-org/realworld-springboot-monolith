package com.example.realworld.application.users.domain;

import com.example.realworld.application.follows.exception.NotFoundFollowException;
import com.example.realworld.application.users.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @DisplayName("사용자 간의 팔로우 조회 실패 테스트")
    @Test
    void when_getUser_expect_fail_not_found_follow_exception() {
        String email = "seokrae@gmail.com";
        String otherUserEmail = "other@gmail.com";

        User author = userRepository.save(User.of(email, "1234", "SR"));
        User otherUser = userRepository.save(User.of(otherUserEmail, "1234", "seok"));

        assertThatExceptionOfType(NotFoundFollowException.class)
                .isThrownBy(() -> author.findFollowing(otherUser));
    }
}