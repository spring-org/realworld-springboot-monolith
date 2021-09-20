package com.example.realworld.application.users.dto;

import com.example.realworld.application.users.domain.User;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class RequestSaveUser {

    @NotEmpty
    private final String email;
    @NotEmpty
    private final String userName;
    @NotEmpty
    private final String password;

    private RequestSaveUser(String email, String userName, String password) {
        this.email = email;
        this.userName = userName;
        this.password = password;
    }

    public static RequestSaveUser of(String email, String userName, String password) {
        return new RequestSaveUser(email, userName, password);
    }

    public static User toEntity(RequestSaveUser saveUser) {
        return User.of(saveUser.getEmail(), saveUser.getPassword(), saveUser.getUserName());
    }
}
