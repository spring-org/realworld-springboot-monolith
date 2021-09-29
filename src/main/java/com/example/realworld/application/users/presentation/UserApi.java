package com.example.realworld.application.users.presentation;

import com.example.realworld.application.users.dto.RequestUpdateUser;
import com.example.realworld.application.users.dto.ResponseUser;
import com.example.realworld.application.users.service.UserService;
import com.example.realworld.core.exception.UnauthorizedUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/user")
@RequiredArgsConstructor
public class UserApi {

    public static final String EMAIL = "email";
    private final UserService userService;

    /**
     * 현재 사용자의 정보를 조회
     *
     * @param session 현재 사용자의 정보를 갖는 세션
     * @return 사용자의 정보를 반환
     */
    @GetMapping
    public ResponseEntity<ResponseUser> getCurrentUser(HttpSession session) {

        String email = (String) session.getAttribute(EMAIL);
        if (Strings.isEmpty(email)) {
            throw new UnauthorizedUserException("접근 권한이 부족합니다.");
        }
        ResponseUser responseUser = userService.getUserByEmail(email);

        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

    /**
     * 현재 사용자의 정보를 수정
     *
     * @param session    현재 사용자의 정보를 갖는 세션
     * @param updateUser 사용의 특정 정보를 수정하기 위한 정보
     * @return 수정된 사용자의 정보를 반환
     */
    @PutMapping
    public ResponseEntity<ResponseUser> putUser(
            HttpSession session, @Valid @RequestBody RequestUpdateUser updateUser) {

        String email = (String) session.getAttribute(EMAIL);
        if (Strings.isEmpty(email) || !email.equals(updateUser.getEmail())) {
            throw new UnauthorizedUserException("접근 권한이 부족합니다.");
        }

        ResponseUser responseUser = userService.updateUser(email, updateUser);

        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

}
