package com.example.realworld.application.users.dto;

import com.example.realworld.application.users.persistence.FollowUserRelationShip;
import com.example.realworld.application.users.persistence.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonPropertyOrder({"email", "userName", "bio", "image", "following"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseProfile {

    private final String email;
    private final String userName;
    private final String bio;
    private final String image;
    private final boolean following;

    private ResponseProfile(final User author, final boolean following) {
        this.email = author.getEmail();
        this.userName = author.profile().userName();
        this.bio = author.profile().bio();
        this.image = author.profile().image();
        this.following = following;
    }

    public static ResponseProfile of(final User fromUser) {
        return new ResponseProfile(fromUser,
		        fromUser.profile().isFollowing());
    }

    public static ResponseProfile ofProfile(final User fromUser, final User toUser) {
        FollowUserRelationShip relationShip = new FollowUserRelationShip(fromUser);
        return new ResponseProfile(toUser, relationShip.isFollowing(toUser));
    }
}
