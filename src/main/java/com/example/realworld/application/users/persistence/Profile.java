package com.example.realworld.application.users.persistence;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile implements Serializable {

    private String userName;

    private String bio;

    private String image;

    private boolean following;

    private Profile(String userName) {
        this(userName, null, null, false);
    }

    private Profile(String userName, String bio, String image, boolean following) {
        this.userName = userName;
        this.bio = bio;
        this.image = image;
        this.following = following;
    }

    public static Profile from(String userName) {
        return new Profile(userName);
    }

    public static Profile of(String userName, String bio, String image, boolean following) {
        return new Profile(userName, bio, image, following);
    }

    public String userName() {
        return userName;
    }

    public String bio() {
        return bio;
    }

    public String image() {
        return image;
    }

    public boolean isFollowing() {
        return following;
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

    public void changeFollowing(boolean following) {
        this.following = following;
    }
}
