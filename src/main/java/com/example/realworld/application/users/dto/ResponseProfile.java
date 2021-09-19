package com.example.realworld.application.users.dto;

import com.example.realworld.application.users.domain.Profile;
import com.example.realworld.application.users.domain.User;
import lombok.Getter;

@Getter
public class ResponseProfile {

    private final String userName;
    private final String bio;
    private final String image;
    private final boolean following;

    private ResponseProfile(Profile profile, boolean following) {
        this.userName = profile.getUserName();
        this.bio = profile.getBio();
        this.image = profile.getImage();
        this.following = following;
    }

    // TODO following 값은 어떻게 보여줘야 할까?
    public static ResponseProfile of(User user) {
        return new ResponseProfile(user.getProfile(), false);
    }
}
