package com.example.realworld.application.users.api;

import com.example.realworld.application.users.dto.RequestLoginUser;
import com.example.realworld.application.users.dto.RequestSaveUser;
import com.example.realworld.application.users.dto.ResponseUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/users")
public class AuthApi {

    @PostMapping(value = "/login")
    public ResponseEntity<ResponseUser> login(
            @Valid @RequestBody RequestLoginUser loginUser) {

        ResponseUser responseUser = new ResponseUser();
        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

    @PostMapping
    public ResponseEntity<ResponseUser> registerUser(
            @Valid @RequestBody RequestSaveUser saveUser) {

        ResponseUser responseUser = new ResponseUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }
}
