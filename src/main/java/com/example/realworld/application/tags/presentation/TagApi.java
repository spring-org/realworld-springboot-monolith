package com.example.realworld.application.tags.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "/api/tags")
public class TagApi {

    /**
     * 인증 필요 X
     *
     * @return List of Tags
     */
    @GetMapping
    public ResponseEntity<?> getTags() {
        return ResponseEntity.status(HttpStatus.OK).body("{}");
    }
}
