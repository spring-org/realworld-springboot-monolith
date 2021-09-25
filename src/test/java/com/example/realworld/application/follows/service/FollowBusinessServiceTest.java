package com.example.realworld.application.follows.service;

import com.example.realworld.application.follows.exception.DuplicateFollowException;
import com.example.realworld.application.follows.exception.NotFoundFollowException;
import com.example.realworld.application.follows.repository.FollowRepository;
import com.example.realworld.application.users.domain.User;
import com.example.realworld.application.users.dto.ResponseProfile;
import com.example.realworld.application.users.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FollowBusinessServiceTest {

    @Autowired
    private FollowBusinessService followBusinessService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        followRepository.deleteAll();
    }

    @DisplayName("사용자 간의 팔로우 테스트")
    @Test
    void when_follow_expect_success_user_relationship() {
        // given
        User fromUser = User.of("seok1@gmail.com", "1234", "seok1");
        User toUser = User.of("seok2@gmail.com", "1234", "seok2");

        userRepository.save(fromUser);
        userRepository.save(toUser);

        // when
        ResponseProfile responseProfile = followBusinessService.followUser(fromUser.getEmail(), toUser.getEmail());

        // then
        assertThat(responseProfile.isFollowing()).isTrue();
    }

    @DisplayName("사용자 간의 팔로우 예외 테스트")
    @Test
    void when_follow_expect_fail_duplicate_follow() {
        // given
        User fromUser = User.of("seok1@gmail.com", "1234", "seok1");
        User toUser = User.of("seok2@gmail.com", "1234", "seok2");

        userRepository.save(fromUser);
        userRepository.save(toUser);

        // when
        String fromEmail = fromUser.getEmail();
        String toEmail = toUser.getEmail();
        followBusinessService.followUser(fromEmail, toEmail);

        // then
        assertThatExceptionOfType(DuplicateFollowException.class)
                .isThrownBy(() -> followBusinessService.followUser(fromEmail, toEmail));
    }

    @DisplayName("사용자 간의 언팔로우 테스트")
    @Test
    void when_unFollow_expect_success_single() {
        // given
        User fromUser = User.of("seok1@gmail.com", "1234", "seok1");
        User toUser = User.of("seok2@gmail.com", "1234", "seok2");

        User savedFromUser = userRepository.save(fromUser);
        userRepository.save(toUser);

        // when
        followBusinessService.followUser(fromUser.getEmail(), toUser.getEmail());
        followBusinessService.unFollow(fromUser.getEmail(), toUser.getEmail());

        // then
        assertThat(savedFromUser.isFollowing(toUser)).isFalse();
    }

    @DisplayName("사용자 간의 언팔로우 예외 테스트")
    @Test
    void when_unFollow_expect_fail_not_found_follow_exception() {
        // given
        User fromUser = User.of("seok1@gmail.com", "1234", "seok1");
        User toUser = User.of("seok2@gmail.com", "1234", "seok2");

        User savedFromUser = userRepository.save(fromUser);
        User savedToUser = userRepository.save(toUser);

        // when
        String fromEmail = savedFromUser.getEmail();
        String toEmail = savedToUser.getEmail();

        // then
        assertThatExceptionOfType(NotFoundFollowException.class)
                .isThrownBy(() -> followBusinessService.unFollow(fromEmail, toEmail));
    }
}