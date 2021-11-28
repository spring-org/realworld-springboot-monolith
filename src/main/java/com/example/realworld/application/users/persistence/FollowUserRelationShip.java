package com.example.realworld.application.users.persistence;

import com.example.realworld.application.follows.persistence.Follow;

import java.io.Serializable;

public final class FollowUserRelationShip implements Serializable {

    private final User user;

    public FollowUserRelationShip(User user) {
        this.user = user;
    }

    public User user() {
        return user;
    }

    public Follows follows() {
        return user.getFollows();
    }// 팔로우 추가

    public void following(Follow follower) {
        user.getFollows().addFollowing(follower);
    }

    public void followers(Follow followee) {
        user.getFollows().addFollowers(followee);
        updateFollowFlag(followee.toUser());
    }

    // 언팔
    public void unFollowing(Follow follower) {
        user.getFollows().removeFollowing(follower);
    }

    public void unFollowers(Follow followee) {
        user.getFollows().removeFollowers(followee);
        updateFollowFlag(followee.toUser());
    }

    void updateFollowFlag(User toUser) {
        boolean anyMatch = user.getFollows().isFollowers(toUser);
        user.getProfile().changeFollowing(anyMatch);
    }

    // 팔로우 관계인지 확인
    public boolean isFollowing(User toUser) {
        return user.getFollows().isFollowing(toUser);
    }

    // 팔로우 관계를 찾기위한 검색
    public Follow findFollowing(User toUser) {
        return user.getFollows().findFollowing(toUser);
    }
}