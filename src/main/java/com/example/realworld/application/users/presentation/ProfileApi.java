package com.example.realworld.application.users.presentation;

import com.example.realworld.application.follows.service.FollowService;
import com.example.realworld.application.users.dto.ResponseProfile;
import com.example.realworld.application.users.service.UserService;
import com.example.realworld.core.security.context.UserDetailsContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping(value = "/api/profiles")
@RequiredArgsConstructor
public class ProfileApi {

    private final UserService userService;
    private final FollowService followService;

    /**
     * 특정 사용자의 프로필 조회
     *
     * @param userDetailsContext 현재 사용자의 정보
     * @param toEmail            특정 사용자의 이메일 정보
     * @return 특정 사용자의 프로필 정보 반환
     */
    @GetMapping(value = "/{toEmail}")
    public ResponseEntity<ResponseProfile> getProfile(
            @AuthenticationPrincipal UserDetailsContext userDetailsContext,
            @PathVariable("toEmail") String toEmail) {

        ResponseProfile responseProfile;

        if (Objects.nonNull(userDetailsContext)) {
            String currentUserEmail = userDetailsContext.getUsername();
            responseProfile = userService.getProfile(currentUserEmail, toEmail);
            return ResponseEntity.status(HttpStatus.OK).body(responseProfile);
        }
        responseProfile = userService.getProfile(toEmail);
        return ResponseEntity.status(HttpStatus.OK).body(responseProfile);
    }

    /**
     * 특정 사용자와 팔로우 관계 생성
     *
     * @param userDetailsContext 현재 사용자의 정보
     * @param toEmail            특정 사용자의 이메일 정보
     * @return 특정 사용자와의 팔로우 관계 및 프로필 정보를 반환
     */
    @PostMapping(value = "/{toEmail}/follow")
    public ResponseEntity<ResponseProfile> postFollowUser(
            @AuthenticationPrincipal UserDetailsContext userDetailsContext,
            @PathVariable("toEmail") String toEmail) {

        String currentUserEmail = userDetailsContext.getUsername();
        ResponseProfile responseProfile = followService.follow(currentUserEmail, toEmail);

        return ResponseEntity.status(HttpStatus.OK).body(responseProfile);
    }

    /**
     * 특정 사용자와의 팔로우 관계를 취소
     *
     * @param userDetailsContext 현재 사용자의 정보
     * @param toEmail            특정 사용자의 이메일 정보
     * @return 특정 사용자와의 팔로우 관계 및 프로필 정보를 반환
     */
    @DeleteMapping(value = "/{toEmail}/follow")
    public ResponseEntity<ResponseProfile> deleteUnFollowUser(
            @AuthenticationPrincipal UserDetailsContext userDetailsContext,
            @PathVariable("toEmail") String toEmail) {

        String currentUserEmail = userDetailsContext.getUsername();

        ResponseProfile responseProfile = followService.unFollow(currentUserEmail, toEmail);

        return ResponseEntity.status(HttpStatus.OK).body(responseProfile);
    }
}
