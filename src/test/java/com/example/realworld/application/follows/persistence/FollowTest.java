package com.example.realworld.application.follows.persistence;

import com.example.realworld.application.follows.exception.CannotSelfFollowException;
import com.example.realworld.application.users.persistence.FollowUserRelationShip;
import com.example.realworld.application.users.persistence.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.realworld.application.users.UserFixture.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class FollowTest {

    @DisplayName("A 사용자가 B 사용자를 팔로우하는 확인 테스트")
    @Test
    void when_createFollow_expected_user_relations() {
        // given
        User fromUser = createUser("fromUser@gmail.com");
        User toUser = createUser("toUser@gmail.com");

        // when
        FollowUserRelationShip relationShip = new FollowUserRelationShip(fromUser);
        relationShip.following(FollowFactory.following(fromUser, toUser));

        // then
        assertThat(relationShip.isFollowing(toUser)).isTrue();
    }

    @DisplayName("A 사용자가 B 사용자를 언팔로우하는 확인 테스트")
    @Test
    void when_unFollow_expected_delete_user_relations() {
        // given
        User fromUser = createUser("fromUser@gmail.com");
        User toUser = createUser("toUser@gmail.com");
        // when
        FollowUserRelationShip relationShip = new FollowUserRelationShip(fromUser);
        Follow following = FollowFactory.following(fromUser, toUser);
        // then
        relationShip.following(following);
        assertThat(relationShip.isFollowing(toUser)).isTrue();
        relationShip.unFollowing(following);
        assertThat(relationShip.isFollowing(toUser)).isFalse();
    }

    @DisplayName("동일한 사용자가 follow 하는 경우 예외 테스트")
    @Test
    void when_follow_expected_same_user_exception() {
        User fromUser = createUser("fromUser@gmail.com");

        assertThatExceptionOfType(CannotSelfFollowException.class)
                .isThrownBy(() -> FollowFactory.following(fromUser, fromUser));
    }
}