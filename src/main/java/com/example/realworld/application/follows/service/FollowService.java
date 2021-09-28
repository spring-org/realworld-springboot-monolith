package com.example.realworld.application.follows.service;

import com.example.realworld.application.users.dto.ResponseProfile;

public interface FollowService {
    // 특정 사용자와의 팔로우 관계 등록
    ResponseProfile follow(String fromEmail, String toEmail);

    // 팔로우 관계에서 특정 사용자 제외
    ResponseProfile unFollow(String fromEmail, String toEmail);
}
