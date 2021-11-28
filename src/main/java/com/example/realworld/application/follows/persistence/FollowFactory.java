package com.example.realworld.application.follows.persistence;

import com.example.realworld.application.users.persistence.User;

public class FollowFactory {

    private FollowFactory() {
    }

    public static Follow following(User sourceUser, User destUser) {
        return new Follow(sourceUser, destUser);
    }
}
