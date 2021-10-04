package com.example.realworld.application.users.presentation;

import com.example.realworld.application.follows.service.FollowService;
import com.example.realworld.application.users.dto.ResponseProfile;
import com.example.realworld.application.users.exception.UnauthorizedUserException;
import com.example.realworld.application.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/api/profiles")
@RequiredArgsConstructor
public class ProfileApi {

    public static final String EMAIL = "email";
    private final UserService userService;
    private final FollowService followService;

    /**
     * 특정 사용자의 프로필 조회
     *
     * @param toEmail 특정 사용자의 이메일 정보
     * @return 특정 사용자의 프로필 정보 반환
     */
    @GetMapping(value = "/{toEmail}")
    public ResponseEntity<ResponseProfile> getProfile(
            HttpSession session, @PathVariable("toEmail") String toEmail) {

        ResponseProfile responseProfile;

        String currentUserEmail = (String) session.getAttribute(EMAIL);
        if (!Strings.isEmpty(currentUserEmail)) {
            responseProfile = userService.getProfile(currentUserEmail, toEmail);
            return ResponseEntity.status(HttpStatus.OK).body(responseProfile);
        }
        responseProfile = userService.getProfile(toEmail);
        return ResponseEntity.status(HttpStatus.OK).body(responseProfile);
    }

    /**
     * 특정 사용자와 팔로우 관계 생성
     *
     * @param session 현재 사용자의 정보를 갖는 세션
     * @param toEmail 특정 사용자의 이메일 정보
     * @return 특정 사용자와의 팔로우 관계 및 프로필 정보를 반환
     */
    @PostMapping(value = "/{toEmail}/follow")
    public ResponseEntity<ResponseProfile> postFollowUser(
            HttpSession session, @PathVariable("toEmail") String toEmail) {

        String currentUserEmail = (String) session.getAttribute(EMAIL);
        if (Strings.isEmpty(currentUserEmail)) {
            throw new UnauthorizedUserException();
        }

        ResponseProfile responseProfile = followService.follow(currentUserEmail, toEmail);

        return ResponseEntity.status(HttpStatus.OK).body(responseProfile);
    }

    /**
     * 특정 사용자와의 팔로우 관계를 취소
     *
     * @param session 현재 사용자의 정보를 갖는 세션
     * @param toEmail 특정 사용자의 이메일 정보
     * @return 특정 사용자와의 팔로우 관계 및 프로필 정보를 반환
     */
    @DeleteMapping(value = "/{toEmail}/follow")
    public ResponseEntity<ResponseProfile> deleteUnFollowUser(
            HttpSession session, @PathVariable("toEmail") String toEmail) {

        String currentUserEmail = (String) session.getAttribute(EMAIL);
        if (Strings.isEmpty(currentUserEmail)) {
            throw new UnauthorizedUserException();
        }

        ResponseProfile responseProfile = followService.unFollow(currentUserEmail, toEmail);

        return ResponseEntity.status(HttpStatus.OK).body(responseProfile);
    }
}
