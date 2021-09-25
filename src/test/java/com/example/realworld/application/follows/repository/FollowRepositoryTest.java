package com.example.realworld.application.follows.repository;

import com.example.realworld.application.follows.domain.Follow;
import com.example.realworld.application.users.domain.User;
import com.example.realworld.application.users.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FollowRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    private static Stream<Arguments> userList() {
        return Stream.of(
                Arguments.of(
                        User.of("seok2@gmail.com", "1234"),
                        User.of("seok3@gmail.com", "1234"),
                        User.of("seok1@gmail.com", "1234"))
        );
    }

    @DisplayName("사용자 간의 Follow 테스트")
    @ParameterizedTest(name = "fromUser : {0}, toUser : {1} 팔로우 테스트")
    @MethodSource(value = "userList")
    void when_follow_expected_following(final User fromUser, final User toUser) {
        // given
        userRepository.saveAll(List.of(fromUser, toUser));

        final Follow newFollow = Follow.following(fromUser, toUser);
        Follow savedFollow = followRepository.save(newFollow);
        fromUser.getFollowing().add(savedFollow);

        // when
        boolean contains = fromUser.isFollowing(toUser);
        long followSize = fromUser.getFollowing().size();

        // then
        assertThat(contains).isTrue();
        assertThat(followSize).isNotZero();
    }

    @DisplayName("사용자 간의 unFollow 확인 테스트")
    @ParameterizedTest(name = "fromUser : {0}, toUser : {1} unFollow 확인 테스트")
    @MethodSource(value = "userList")
    void when_follow_expected_throw_not_matches_toUser_email(final User fromUser, final User toUser, final User otherUser) {
        // given
        userRepository.saveAll(List.of(fromUser, toUser, otherUser));

        final Follow newFollow = Follow.following(fromUser, toUser);
        followRepository.save(newFollow);
        fromUser.follow(newFollow);

        // when
        boolean contains = fromUser.isFollowing(otherUser);

        // then
        assertThat(contains).isFalse();
    }
}