package com.example.realworld.application.users.dto;

import com.example.realworld.application.users.persistence.Profile;
import com.example.realworld.application.users.persistence.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;

@Getter
@ToString
@JsonTypeName(value = "user")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = WRAPPER_OBJECT)
@JsonPropertyOrder({"email", "userName", "bio", "image", "token"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUser {

    private final String email;
    private final String userName;
    private final String bio;
    private final String image;
    private final String token;

    private ResponseUser(String email, String userName, Profile profile, String token) {
        this.email = email;
        this.userName = userName;
        this.bio = profile.bio();
        this.image = profile.image();
        this.token = token;
    }

    public static ResponseUser of(final User user) {
        return new ResponseUser(user.getEmail(), user.userName(), user.getProfile(), user.getToken());
    }
}
