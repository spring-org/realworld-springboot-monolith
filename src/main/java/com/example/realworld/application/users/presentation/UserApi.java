package com.example.realworld.application.users.presentation;

import com.example.realworld.application.users.dto.RequestUpdateUser;
import com.example.realworld.application.users.dto.ResponseUser;
import com.example.realworld.application.users.exception.UnauthorizedUserException;
import com.example.realworld.application.users.service.UserService;
import com.example.realworld.core.security.context.UserDetailsContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/user")
@RequiredArgsConstructor
public class UserApi {

    private final UserService userService;

    /**
     * 현재 사용자의 정보를 조회
     *
     * @param userDetailsContext 현재 사용자의 정보
     * @return 사용자의 정보를 반환
     */
    @GetMapping
    public ResponseEntity<ResponseUser> getCurrentUser(
            @AuthenticationPrincipal UserDetailsContext userDetailsContext) {

        String email = userDetailsContext.getUsername();
        ResponseUser responseUser = userService.getUserByEmail(email);

        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

    /**
     * 현재 사용자의 정보를 수정
     *
     * @param userDetailsContext 현재 사용자의 정보
     * @param updateUser         사용의 특정 정보를 수정하기 위한 정보
     * @return 수정된 사용자의 정보를 반환
     */
    @PutMapping
    public ResponseEntity<ResponseUser> putUser(
            @AuthenticationPrincipal UserDetailsContext userDetailsContext,
            @Valid @RequestBody RequestUpdateUser updateUser) {

        String email = userDetailsContext.getUsername();
        // 일단 이메일 정보가 일치해야 수정이 가능한 것으로 간주.
        if (!email.equals(updateUser.getEmail())) {
            throw new UnauthorizedUserException();
        }

        ResponseUser responseUser = userService.updateUser(email, updateUser);

        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

}
