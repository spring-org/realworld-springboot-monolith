package com.example.realworld.application.users.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile implements Serializable {

    @Column(nullable = false, unique = true)
    private String userName;

    private String bio;

    private String image;

    private Profile(String userName, String bio, String image) {
        this.userName = userName;
        this.bio = bio;
        this.image = image;
    }

    public static Profile createProfile(String userName) {
        return new Profile(userName, null, null);
    }

    public static Profile of(String userName, String bio, String image) {
        return new Profile(userName, bio, image);
    }

    public void changeUserName(String userName) {
        this.userName = userName;
    }

    public void changeImage(String image) {
        this.image = image;
    }

    public void changeBio(String bio) {
        this.bio = bio;
    }
}
