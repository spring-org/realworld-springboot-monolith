package com.example.realworld.application.follow.domain;

import com.example.realworld.application.users.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FollowTest {

    public User createUser(Long id) {
        return User.registeredUser(id, "seok" + id + "@gmail.com", "1234");
    }

    @DisplayName("A 사용자가 B 사용자를 팔로우하는 확인 테스트")
    @Test
    void when_createFollow_expected_user_relations() {
        // when
        User fromUser = createUser(1L);
        User toUser = createUser(2L);

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
        User fromUser = createUser(1L);
        User toUser = createUser(2L);

        final Follow following = Follow.following(fromUser, toUser);
        fromUser.follow(following);

        boolean isFollow = fromUser.isFollowing(toUser);
        assertThat(isFollow).isTrue();

        // given
        fromUser.unFollow(toUser);
        boolean isUnFollow = fromUser.isFollowing(toUser);

        // then
        assertThat(isUnFollow).isFalse();
    }
}