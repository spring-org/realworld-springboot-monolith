package com.example.realworld.application.users.service;

import com.example.realworld.application.users.dto.*;

public interface UserService {
    // 사용자 등록
    ResponseUser postUser(final RequestSaveUser saveUser);

    // 사용자 수정
    ResponseUser updateUser(String email, RequestUpdateUser updateUser);

    // 사용자 조회(이메일)
    ResponseUser getUserByEmail(String email);

    // 사용자 조회(프로필)
    ResponseProfile getProfile(String toEmail);

    ResponseProfile getProfile(String currentUserEmail, String toEmail);

    ResponseUser login(RequestLoginUser loginUser);
}
