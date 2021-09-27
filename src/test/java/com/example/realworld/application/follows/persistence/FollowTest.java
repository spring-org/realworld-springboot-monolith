package com.example.realworld.application.follows.persistence;

import com.example.realworld.application.users.persistence.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FollowTest {

    public User createUser(String email) {
        return User.of(email, "1234");
    }

    @DisplayName("A 사용자가 B 사용자를 팔로우하는 확인 테스트")
    @Test
    void when_createFollow_expected_user_relations() {
        // when
        User fromUser = createUser("seokrae@gmail.com");
        User toUser = createUser("seok2@gmail.com");

        final Follow following = Follow.following(fromUser, toUser);
        fromUser.follow(following);

        // given
        boolean isFollow = fromUser.isFollowing(toUser);

        // then
        assertThat(isFollow).isTrue();
    }

    @DisplayName("A 사용자가 B 사용자를 언팔로우하는 확인 테스트")
    @Test
    void when_unFollow_expected_delete_user_relations() {
        // when
        User fromUser = createUser("seokrae@gmail.com");
        User toUser = createUser("seok2@gmail.com");

        final Follow following = Follow.following(fromUser, toUser);
        fromUser.follow(following);

        boolean isFollow = fromUser.isFollowing(toUser);
        assertThat(isFollow).isTrue();

        // given
        fromUser.unFollow(following);
        boolean isUnFollow = fromUser.isFollowing(toUser);

        // then
        assertThat(isUnFollow).isFalse();
    }
}