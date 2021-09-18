package com.example.realworld.application.users.dto;

import com.example.realworld.application.users.domain.Profile;
import lombok.Getter;

@Getter
public class ResponseProfile {

    private final String userName;
    private final String bio;
    private final String image;
    private final boolean following;

    private ResponseProfile(String userName, String bio, String image, boolean following) {
        this.userName = userName;
        this.bio = bio;
        this.image = image;
        this.following = following;
    }

    public static ResponseProfile of(Profile profile) {
        return new ResponseProfile(profile.getUserName(), profile.getBio(), profile.getImage(), false);
    }
}
