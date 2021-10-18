package com.example.realworld.application.users.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestLoginUser {

    @NotEmpty(message = "email is not empty")
    private String email;
    @NotEmpty(message = "password is not empty")
    private String password;

    private RequestLoginUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static RequestLoginUser of(String email, String password) {
        return new RequestLoginUser(email, password);
    }
}
