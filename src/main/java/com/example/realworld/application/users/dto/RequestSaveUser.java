package com.example.realworld.application.users.dto;

import javax.validation.constraints.NotEmpty;

public class RequestSaveUser {

    @NotEmpty
    private String email;
    @NotEmpty
    private String userName;
    @NotEmpty
    private String password;
}
