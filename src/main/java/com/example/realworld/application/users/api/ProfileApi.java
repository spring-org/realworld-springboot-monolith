package com.example.realworld.application.users.api;

import com.example.realworld.application.users.dto.ResponseProfile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/profiles")
public class ProfileApi {

    @GetMapping(value = "/{username}")
    public ResponseEntity<ResponseProfile> getProfile(@PathVariable("username") String username) {

        ResponseProfile responseProfile = new ResponseProfile();
        return ResponseEntity.status(HttpStatus.OK).body(responseProfile);
    }

    @PostMapping(value = "/{username}/follow")
    public ResponseEntity<ResponseProfile> postFollowUser(@PathVariable("username") String username) {

        ResponseProfile responseProfile = new ResponseProfile();
        return ResponseEntity.status(HttpStatus.OK).body(responseProfile);
    }

    @DeleteMapping(value = "/{username}/follow")
    public ResponseEntity<ResponseProfile> deleteUnFollowUser(@PathVariable("username") String username) {

        ResponseProfile responseProfile = new ResponseProfile();
        return ResponseEntity.status(HttpStatus.OK).body(responseProfile);
    }
}
