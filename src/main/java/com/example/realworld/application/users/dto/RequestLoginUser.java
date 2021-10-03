package com.example.realworld.application.users.dto;

import com.example.realworld.application.users.persistence.User;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@ToString
public class RequestLoginUser {

    @NotEmpty(message = "email is not empty")
    private final String email;
    @NotEmpty(message = "password is not empty")
    private final String password;

    private RequestLoginUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static RequestLoginUser of(String email, String password) {
        return new RequestLoginUser(email, password);
    }

    public static User toEntity(RequestLoginUser loginUser) {
        return User.of(loginUser.getEmail(), loginUser.getPassword());
    }
}
