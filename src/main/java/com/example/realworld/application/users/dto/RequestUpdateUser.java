package com.example.realworld.application.users.dto;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.Optional;

@Getter
@ToString
public class RequestUpdateUser {

    @NotEmpty
    private final String email;
    private final String userName;
    private final String password;
    private final String image;
    private final String bio;

    private RequestUpdateUser(String email, String userName, String password, String image, String bio) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.image = image;
        this.bio = bio;
    }

    public static RequestUpdateUser of(String email, String userName, String password, String image, String bio) {
        return new RequestUpdateUser(email, userName, password, image, bio);
    }
}
