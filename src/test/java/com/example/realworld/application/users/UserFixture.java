package com.example.realworld.application.users;

import com.example.realworld.application.users.dto.RequestLoginUser;
import com.example.realworld.application.users.dto.RequestSaveUser;
import com.example.realworld.application.users.dto.RequestUpdateUser;
import com.example.realworld.application.users.persistence.Profile;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.application.users.persistence.UserFactory;

public class UserFixture {

    private UserFixture() {
    }

    public static RequestUpdateUser getRequestUpdateUser(String updatedUserName, String password, String image, String bio) {
        return RequestUpdateUser.of(updatedUserName, password, image, bio);
    }

    public static RequestSaveUser getRequestSaveUser(String email, String userName) {
        return RequestSaveUser.of(email, userName, "1234");
    }

    public static User createUser(String email) {
        return UserFactory.of(email, "1234", "seokrae");
    }

    public static User createUser(String email, String username) {
        return UserFactory.of(email, "1234", username);
    }

    public static Profile createProfile() {
        return Profile.of("seokrae", "bio hello", null, false);
    }

    public static RequestLoginUser getRequestLoginUser(String email) {
        return RequestLoginUser.of(email, "1234");
    }
}
