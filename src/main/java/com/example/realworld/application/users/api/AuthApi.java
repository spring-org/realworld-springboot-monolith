package com.example.realworld.application.users.api;

import com.example.realworld.application.users.domain.User;
import com.example.realworld.application.users.dto.RequestLoginUser;
import com.example.realworld.application.users.dto.RequestSaveUser;
import com.example.realworld.application.users.dto.ResponseUser;
import com.example.realworld.application.users.service.UserBusinessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/api/users")
@RequiredArgsConstructor
public class AuthApi {

    private final UserBusinessService userBusinessService;

    @PostMapping
    public ResponseEntity<ResponseUser> signUpUser(
            @Valid @RequestBody RequestSaveUser saveUser) {

        ResponseUser responseUser = userBusinessService.addUser(saveUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<ResponseUser> login(
            HttpSession session, @Valid @RequestBody RequestLoginUser loginUser) {

        User userByEmail = userBusinessService.findUserByEmail(loginUser.getEmail());
        session.setAttribute("email", userByEmail.getEmail());
        ResponseUser responseUser = ResponseUser.of(RequestLoginUser.toEntity(loginUser));

        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {

        session.removeAttribute("email");

        return ResponseEntity.noContent().build();
    }
}
