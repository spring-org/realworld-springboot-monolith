package com.example.realworld.application.users.dto;

import com.example.realworld.application.users.domain.User;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@ToString
public class RequestLoginUser {

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    public static User toEntity(RequestLoginUser loginUser) {
        return User.of(loginUser.getEmail(), loginUser.getPassword());
    }
}
