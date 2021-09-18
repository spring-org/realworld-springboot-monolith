package com.example.realworld.application.users.dto;

import com.example.realworld.application.users.domain.Profile;
import com.example.realworld.application.users.domain.User;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class RequestSaveUser {

    @NotEmpty
    private String email;
    @NotEmpty
    private String userName;
    @NotEmpty
    private String password;

    public static User toEntity(RequestSaveUser saveUser) {
        return User.of(saveUser.getEmail(), saveUser.getPassword(), Profile.createProfile(saveUser.getUserName()));
    }
}
