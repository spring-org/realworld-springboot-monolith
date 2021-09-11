package com.example.realworld.application.users.api;

import com.example.realworld.application.users.dto.RequestUpdateUser;
import com.example.realworld.application.users.dto.ResponseUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/user")
public class UserApi {

    @GetMapping
    public ResponseEntity<ResponseUser> getCurrentUser() {

        ResponseUser responseUser = new ResponseUser();
        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

    @PutMapping
    public ResponseEntity<ResponseUser> putUser(
            @Valid @RequestBody RequestUpdateUser updateUser) {

        ResponseUser responseUser = new ResponseUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

}
