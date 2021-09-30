package com.example.realworld.application.users.presentation;

import com.example.realworld.application.users.dto.RequestLoginUser;
import com.example.realworld.application.users.dto.RequestSaveUser;
import com.example.realworld.application.users.dto.ResponseUser;
import com.example.realworld.application.users.service.UserBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/users")
@RequiredArgsConstructor
public class AuthApi {

    public static final String EMAIL = "email";
    private final UserBusinessService userBusinessService;

    /**
     * 새로운 사용자 등록
     *
     * @param saveUser 새로운 사용자 등록을 위한 정보
     * @return 새로 등록된 사용자의 정보를 반환
     */
    @PostMapping
    public ResponseEntity<ResponseUser> signUpUser(
            @Valid @RequestBody RequestSaveUser saveUser) {

        ResponseUser responseUser = userBusinessService.postUser(saveUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    /**
     * 사용자 로그인
     *
     * @param session   사용자 로그인 시 세션에 사용자의 정보를 등록하기 위한 세션
     * @param loginUser 사용자 로그인을 위한 정보
     * @return 로그인한 사용자의 정보를 반환
     */
    @PostMapping(value = "/login")
    public ResponseEntity<ResponseUser> login(
            HttpSession session, @Valid @RequestBody RequestLoginUser loginUser) {

        ResponseUser responseUser = userBusinessService.getUserByEmail(loginUser.getEmail());
        session.setAttribute(EMAIL, responseUser.getEmail());

        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

    /**
     * 사용자 로그아웃
     *
     * @param session 현재 사용자의 정보를 갖는 세션
     * @return 204 코드
     */
    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {

        session.removeAttribute(EMAIL);

        return ResponseEntity.noContent().build();
    }
}
