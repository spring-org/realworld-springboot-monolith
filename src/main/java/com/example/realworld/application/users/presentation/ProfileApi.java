package com.example.realworld.application.users.presentation;

import com.example.realworld.application.follows.exception.CannotFollowException;
import com.example.realworld.application.follows.business.FollowService;
import com.example.realworld.application.users.dto.ResponseProfile;
import com.example.realworld.application.users.business.UserBusinessService;
import com.example.realworld.core.exception.UnauthorizedUserException;
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
    private final UserBusinessService userService;
    private final FollowService followService;

    @GetMapping(value = "/{toEmail}")
    public ResponseEntity<ResponseProfile> getProfile(
            @PathVariable("toEmail") String toEmail) {

        ResponseProfile responseProfile = userService.getProfile(toEmail);

        return ResponseEntity.status(HttpStatus.OK).body(responseProfile);
    }

    @PostMapping(value = "/{toEmail}/follow")
    public ResponseEntity<ResponseProfile> postFollowUser(
            HttpSession session, @PathVariable("toEmail") String toEmail) {

        String fromEmail = (String) session.getAttribute(EMAIL);
        if (Strings.isEmpty(fromEmail)) {
            throw new UnauthorizedUserException("접근 권한이 부족합니다.");
        }

        if (fromEmail.equals(toEmail)) {
            throw new CannotFollowException("자기 자신을 팔로우 할 수 없습니다.");
        }
        ResponseProfile responseProfile = followService.followUser(fromEmail, toEmail);

        return ResponseEntity.status(HttpStatus.OK).body(responseProfile);
    }

    @DeleteMapping(value = "/{toEmail}/follow")
    public ResponseEntity<ResponseProfile> deleteUnFollowUser(
            HttpSession session, @PathVariable("toEmail") String toEmail) {

        String fromEmail = (String) session.getAttribute(EMAIL);
        if (Strings.isEmpty(fromEmail)) {
            throw new UnauthorizedUserException("접근 권한이 부족합니다.");
        }

        if (fromEmail.equals(toEmail)) {
            throw new CannotFollowException("팔로우 관계가 아닙니다.");
        }
        ResponseProfile responseProfile = followService.unFollow(fromEmail, toEmail);

        return ResponseEntity.status(HttpStatus.OK).body(responseProfile);
    }
}
