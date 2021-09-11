package com.example.realworld.application.users.dto;

import javax.validation.constraints.NotEmpty;

public class RequestLoginUser {

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}
