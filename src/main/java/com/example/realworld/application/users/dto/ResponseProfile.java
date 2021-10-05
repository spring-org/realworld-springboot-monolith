package com.example.realworld.application.users.dto;

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
@JsonTypeName(value = "profile")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = WRAPPER_OBJECT)
@JsonPropertyOrder({"email", "userName", "bio", "image", "following"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseProfile {

    private final String email;
    private final String userName;
    private final String bio;
    private final String image;
    private final boolean following;

    public ResponseProfile(User author, boolean following) {
        this.email = author.getEmail();
        this.userName = author.profile().userName();
        this.bio = author.profile().bio();
        this.image = author.profile().image();
        this.following = following;
    }

    public static ResponseProfile of(final User fromUser) {
        return new ResponseProfile(fromUser, fromUser.profile().isFollowing());
    }

    public static ResponseProfile ofProfile(final User fromUser, final User toUser) {
        return new ResponseProfile(toUser, fromUser.isFollowing(toUser));
    }
}
