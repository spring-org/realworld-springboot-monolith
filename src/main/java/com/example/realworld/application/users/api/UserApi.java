package com.example.realworld.application.users.api;

import com.example.realworld.application.users.domain.User;
import com.example.realworld.application.users.dto.RequestUpdateUser;
import com.example.realworld.application.users.dto.ResponseUser;
import com.example.realworld.application.users.service.UserBusinessService;
import com.example.realworld.core.exception.UnauthorizedUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/api/user")
@RequiredArgsConstructor
public class UserApi {

    public static final String EMAIL = "email";
    private final UserBusinessService userBusinessService;

    @GetMapping
    public ResponseEntity<ResponseUser> getCurrentUser(HttpSession session) {

        String email = (String) session.getAttribute(EMAIL);
        if (Strings.isEmpty(email)) {
            throw new UnauthorizedUserException("접근 권한이 부족합니다.");
        }

        User userByEmail = userBusinessService.findUserByEmail(email);
        ResponseUser responseUser = ResponseUser.of(userByEmail);

        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

    @PutMapping
    public ResponseEntity<ResponseUser> putUser(
            HttpSession session, @Valid @RequestBody RequestUpdateUser updateUser) {

        String email = (String) session.getAttribute(EMAIL);
        if (Strings.isEmpty(email) || !email.equals(updateUser.getEmail())) {
            throw new UnauthorizedUserException("접근 권한이 부족합니다.");
        }

        ResponseUser responseUser = userBusinessService.updateUser(email, updateUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

}
