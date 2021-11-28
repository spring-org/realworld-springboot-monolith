package com.example.realworld.application.users.persistence;

public class UserFactory {

    private UserFactory() {
    }

    public static User of(String email, String password) {
        return new User(email, password);
    }

    public static User of(String email, String password, String userName) {
        return new User(email, password, Profile.from(userName), Follows.init(), null);
    }
}
