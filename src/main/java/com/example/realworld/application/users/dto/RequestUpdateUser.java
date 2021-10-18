package com.example.realworld.application.users.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestUpdateUser {

    private String userName;
    private String password;
    private String image;
    private String bio;

    private RequestUpdateUser(String userName, String password, String image, String bio) {
        this.userName = userName;
        this.password = password;
        this.image = image;
        this.bio = bio;
    }

    public static RequestUpdateUser of(String userName, String password, String image, String bio) {
        return new RequestUpdateUser(userName, password, image, bio);
    }
}
