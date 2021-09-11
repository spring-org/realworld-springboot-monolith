package com.example.realworld.application.users.domain;

import lombok.*;

import javax.persistence.Embeddable;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    private String userName;

    private String bio;

    private String image;

    @Builder
    public Profile(String userName, String bio, String image) {
        this.userName = userName;
        this.bio = bio;
        this.image = image;
    }

    public Profile changeUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public Profile changeImage(String url) {
        this.image = url;
        return this;
    }

    public Profile changeBio(String bio) {
        this.bio = bio;
        return this;
    }


}
