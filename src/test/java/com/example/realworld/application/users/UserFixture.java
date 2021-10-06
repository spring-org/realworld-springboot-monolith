package com.example.realworld.application.users;

import com.example.realworld.application.users.dto.RequestLoginUser;
import com.example.realworld.application.users.dto.RequestSaveUser;
import com.example.realworld.application.users.dto.RequestUpdateUser;
import com.example.realworld.application.users.persistence.Profile;
import com.example.realworld.application.users.persistence.User;

public class UserFixture {

    private UserFixture() {
    }

    public static RequestUpdateUser getRequestUpdateUser(String email, String updatedUserName, String password, String image, String bio) {
        return RequestUpdateUser.of(email, updatedUserName, password, image, bio);
    }

    public static RequestSaveUser getRequestSaveUser(String email, String userName) {
        return RequestSaveUser.of(email, userName, "1234");
    }

    public static User createUser(String email) {
        return User.of(email, "1234", "seok");
    }

    public static Profile createProfile() {
        return Profile.of("seokrae", "bio hello", null, false);
    }

    public static RequestLoginUser getRequestLoginUser(String email) {
        return RequestLoginUser.of(email, "1234");
    }

    public static RequestSaveUser getRequestSaveUser(String email) {
        return RequestSaveUser.of(email, "seokrae", "1234");
    }

}
