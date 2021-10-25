package com.example.realworld.application.users.presentation;

import com.example.realworld.application.users.dto.RequestSaveUser;
import com.example.realworld.application.users.dto.ResponseUser;
import com.example.realworld.application.users.service.UserBusinessService;
import com.example.realworld.core.security.context.UserDetailsContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/users")
@RequiredArgsConstructor
public class AuthApi {

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
     * 사용자 로그아웃
     *
     * @return 204 코드
     */
    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetailsContext userContext) {
        return ResponseEntity.noContent().build();
    }
}
