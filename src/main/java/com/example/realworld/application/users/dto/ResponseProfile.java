package com.example.realworld.application.users.dto;

import com.example.realworld.application.users.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseProfile {

    private String email;
    private String userName;
    private String bio;
    private String image;
    private boolean following;

    private ResponseProfile(User author, boolean following) {
        this.email = author.getEmail();
        this.userName = author.getProfile().getUserName();
        this.bio = author.getProfile().getBio();
        this.image = author.getProfile().getImage();
        this.following = following;
    }

    // TODO following 값은 어떻게 보여줘야 할까?
    public static ResponseProfile of(User user) {
        return new ResponseProfile(user, false);
    }
}
