package com.example.realworld.application.follows.persistence.repository;

import com.example.realworld.application.follows.persistence.Follow;
import com.example.realworld.application.follows.persistence.FollowFactory;
import com.example.realworld.application.users.persistence.FollowUserRelationShip;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.application.users.persistence.UserFactory;
import com.example.realworld.application.users.persistence.repository.UserRepository;
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
                        UserFactory.of("seok2@gmail.com", "1234"),
                        UserFactory.of("seok3@gmail.com", "1234"),
                        UserFactory.of("seok1@gmail.com", "1234"))
        );
    }

    @DisplayName("사용자 간의 Follow 테스트")
    @ParameterizedTest(name = "fromUser : {0}, toUser : {1} 팔로우 테스트")
    @MethodSource(value = "userList")
    void when_follow_expected_following(final User fromUser, final User toUser) {
        // given
        userRepository.saveAll(List.of(fromUser, toUser));

        FollowUserRelationShip relationShip = new FollowUserRelationShip(fromUser);
        Follow following = FollowFactory.following(fromUser, toUser);
        relationShip.following(following);

        final Follow savedFollow = followRepository.save(following);
        relationShip.follows().addFollowing(savedFollow);
        // when
        boolean contains = relationShip.isFollowing(toUser);
        int followSize = relationShip.follows().followingSize();
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

        FollowUserRelationShip relationShip = new FollowUserRelationShip(fromUser);
        Follow following = FollowFactory.following(fromUser, toUser);

        followRepository.save(following);
        relationShip.following(following);

        // when
        boolean contains = relationShip.isFollowing(otherUser);

        // then
        assertThat(contains).isFalse();
    }
}