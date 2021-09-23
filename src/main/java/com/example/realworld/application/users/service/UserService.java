package com.example.realworld.application.users.service;

import com.example.realworld.application.users.dto.RequestSaveUser;
import com.example.realworld.application.users.dto.RequestUpdateUser;
import com.example.realworld.application.users.dto.ResponseProfile;
import com.example.realworld.application.users.dto.ResponseUser;

public interface UserService {

    ResponseUser addUser(final RequestSaveUser saveUser);

    ResponseUser updateUser(String email, RequestUpdateUser updateUser);

    ResponseUser getUserByEmail(String email);

    ResponseProfile getProfile(String email);

    boolean existsUserByEmail(String email);
}
