package com.example.realworld.application.users.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestUpdateUser {

    @NotNull
    private String userName;
    private String password;
    @NotNull
    private String image;
    @NotNull
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
