package com.example.realworld.application.users.domain;

import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Getter
@ToString
@Table(name = "TB_USER")
@Entity(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", nullable = false)
    private Long id;

    private String email;

    private String password;

    @Embedded
    private Profile profile;

    @Embedded
    private Follow follow;

    private String token;

    @Builder
    public User(Long id, String email, String password, Profile profile, Follow follow, String token) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.profile = profile;
        this.follow = follow;
        this.token = token;
    }

    public static User registeredUser(Long id, String email, String password) {
        return new User(id, email, password, new Profile(), new Follow(), null);
    }

    // profile
    public User updateUser(String email, String password, String userName, String bio, String url) {
        if (StringUtils.hasText(email)) {
            this.email = email;
        }
        if (StringUtils.hasText(password)) {
            this.password = password;
        }
        if (StringUtils.hasText(userName)) {
            profile.changeUserName(userName);
        }
        if (StringUtils.hasText(bio)) {
            profile.changeBio(bio);
        }
        if (StringUtils.hasText(url)) {
            profile.changeImage(url);
        }
        return this;
    }

    // follow
    public void addFollower(User toUser) {
        this.follow.addFollower(toUser);
    }

    public boolean isFollowing(User toUser) {
        return this.follow.isFollowing(toUser);
    }

    public void unFollow(User toUser) {
        this.follow.unFollow(toUser);
    }
}
