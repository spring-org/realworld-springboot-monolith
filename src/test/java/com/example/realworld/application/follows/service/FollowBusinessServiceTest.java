package com.example.realworld.application.follows.service;

import com.example.realworld.application.follows.exception.CannotSelfFollowException;
import com.example.realworld.application.follows.exception.DuplicatedFollowException;
import com.example.realworld.application.follows.exception.NotFoundFollowException;
import com.example.realworld.application.follows.persistence.repository.FollowRepository;
import com.example.realworld.application.users.dto.ResponseProfile;
import com.example.realworld.application.users.persistence.FollowUserRelationShip;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.application.users.persistence.UserFactory;
import com.example.realworld.application.users.persistence.repository.UserRepository;
import com.example.realworld.application.users.service.UserDomainService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.realworld.application.users.UserFixture.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
class FollowBusinessServiceTest {

    @Autowired
    private FollowBusinessService followBusinessService;

    @Autowired
    private UserDomainService userDomainService;

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
        User fromUser = createUser("fromUser@gmail.com");
        User toUser = createUser("toUser@gmail.com");

        userDomainService.save(fromUser);
        userDomainService.save(toUser);
        // when
        ResponseProfile responseProfile =
                followBusinessService.follow(fromUser.getEmail(), toUser.getEmail());
        // then
        assertThat(responseProfile.isFollowing()).isTrue();
    }

    @DisplayName("사용자 간의 팔로우 조회 실패 테스트")
    @Test
    void when_getUser_expect_fail_not_found_follow_exception() {
        String fromUserEmail = "seokrae@gmail.com";
        String toUserEmail = "other@gmail.com";

        User fromUser = userDomainService.save(createUser(fromUserEmail));
        User toUser = userDomainService.save(createUser(toUserEmail));

        FollowUserRelationShip relationShip = new FollowUserRelationShip(fromUser);

        assertThatExceptionOfType(NotFoundFollowException.class)
                .isThrownBy(() -> relationShip.findFollowing(toUser));
    }

    @DisplayName("사용자 간의 팔로우 예외 테스트")
    @Test
    void when_follow_expect_fail_duplicate_follow() {
        // given
        User fromUser = UserFactory.of("seok1@gmail.com", "1234", "seok1");
        User toUser = UserFactory.of("seok2@gmail.com", "1234", "seok2");

        userDomainService.save(fromUser);
        userDomainService.save(toUser);

        // when
        String fromEmail = fromUser.getEmail();
        String toEmail = toUser.getEmail();
        followBusinessService.follow(fromEmail, toEmail);

        // then
        assertThatExceptionOfType(DuplicatedFollowException.class)
                .isThrownBy(() -> followBusinessService.follow(fromEmail, toEmail));
    }

    @DisplayName("사용자 간의 팔로우 예외(Self Follow) 테스트")
    @Test
    void when_follow_expect_fail_self_follow_exception() {
        // given
        User fromUser = createUser("fromUser@gmail.com");
        userDomainService.save(fromUser);
        // when
        String fromEmail = fromUser.getEmail();
        // then
        assertThatExceptionOfType(CannotSelfFollowException.class)
                .isThrownBy(() -> followBusinessService.follow(fromEmail, fromEmail));
    }

    @DisplayName("사용자 간의 언팔로우 테스트")
    @Test
    void when_unFollow_expect_success_single() {
        // given
        User fromUser = UserFactory.of("seok1@gmail.com", "1234", "seok1");
        User toUser = UserFactory.of("seok2@gmail.com", "1234", "seok2");

        User savedFromUser = userDomainService.save(fromUser);
        userDomainService.save(toUser);

        // when
        followBusinessService.follow(fromUser.getEmail(), toUser.getEmail());
        followBusinessService.unFollow(fromUser.getEmail(), toUser.getEmail());

        FollowUserRelationShip relationShip = new FollowUserRelationShip(savedFromUser);

        // then
        assertThat(relationShip.isFollowing(toUser)).isFalse();
    }

    @DisplayName("사용자 간의 언팔로우 예외(Self Follow) 테스트")
    @Test
    void when_unFollow_expect_fail_self_follow_exception() {
        // given
        User fromUser = createUser("fromUser@gmail.com");
        userDomainService.save(fromUser);
        // when
        String fromEmail = fromUser.getEmail();
        // then
        assertThatExceptionOfType(NotFoundFollowException.class)
                .isThrownBy(() -> followBusinessService.unFollow(fromEmail, fromEmail));
    }

}