package com.example.realworld.application.users.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embeddable;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    private String userName;

    private String bio;

    private String image;

    public Profile(String userName, String bio, String image) {
        this.userName = userName;
        this.bio = bio;
        this.image = image;
    }

    public static Profile of(String userName, String bio, String image) {
        return new Profile(userName, bio, image);
    }

    public void changeUserName(String userName) {
        this.userName = userName;
    }

    public void changeImage(String url) {
        this.image = url;
    }

    public void changeBio(String bio) {
        this.bio = bio;
    }
}
