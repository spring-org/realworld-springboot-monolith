package com.example.realworld.application.users.service;

import com.example.realworld.application.users.dto.ResponseProfile;

public interface FollowService {

    ResponseProfile followUser(String fromEmail, String toEmail);

    ResponseProfile unFollow(String fromEmail, String toEmail);
}
