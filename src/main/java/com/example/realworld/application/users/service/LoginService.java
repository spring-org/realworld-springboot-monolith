package com.example.realworld.application.users.service;

import com.example.realworld.application.users.dto.RequestLoginUser;

public interface LoginService {
    void login(final RequestLoginUser loginUser);

    void logout();

    String currentUser();
}
