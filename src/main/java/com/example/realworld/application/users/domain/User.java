package com.example.realworld.application.users.domain;

import com.example.realworld.application.articles.domain.Article;
import com.example.realworld.application.articles.exception.NotFoundArticleException;
import com.example.realworld.application.follow.domain.Follow;
import com.example.realworld.application.follow.exception.NotFoundFollowException;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@ToString
@Table(name = "TB_USER")
@Entity(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "USER_ID", nullable = false)
    private Long id;

    private String email;

    private String password;

    @Embedded
    private Profile profile;

    private String token;

    @OneToMany(mappedBy = "fromUser", orphanRemoval = true)
    @ToString.Exclude
    private final List<Follow> following = new ArrayList<>();

    @OneToMany(mappedBy = "toUser")
    @ToString.Exclude
    private final List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "author", orphanRemoval = true)
    @ToString.Exclude
    private final Set<Article> articles = new HashSet<>();

    @Builder
    public User(String email, String password, Profile profile, String token) {
        this.email = email;
        this.password = password;
        this.profile = profile;
        this.token = token;
    }

    // User
    public static User of(String email, String password) {
        return new User(email, password, new Profile(), null);
    }

    public String userName() {
        return profile.getUserName();
    }

    public boolean isSameUser(User toUser) {
        return this.getEmail().equals(toUser.getEmail());
    }

    // profile
    public void update(String email, String password, String userName, String bio, String url) {
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
    }

    // Follow
    public void follow(Follow newFollow) {
        if (!isFollowing(newFollow.getToUser())) {
            this.following.add(newFollow);
        }
    }

    public void unFollow(User toUser) {
        if (isFollowing(toUser)) {
            Follow findFollow = findFollowing(toUser);
            this.following.remove(findFollow);
        }
    }

    public boolean isFollowing(User toUser) {
        return this.following.stream()
                .anyMatch(follow -> follow.isSameToUser(toUser));
    }

    private Follow findFollowing(User toUser) {
        return this.following.stream()
                .filter(follow -> follow.isSameToUser(toUser))
                .findAny()
                .orElseThrow(() -> new NotFoundFollowException("사용자 간의 팔로우 관계가 존재하지 않습니다."));
    }

    // Article
    public void postArticles(List<Article> articles) {
        this.articles.addAll(articles);
    }

    public void postArticles(Article article) {
        this.articles.add(article);
    }

    public Article findArticleByTitle(String title) {
        return articles.stream()
                .filter(article -> article.isMatches(title))
                .findAny()
                .orElseThrow(() -> new NotFoundArticleException("존재하지 않는 글입니다."));
    }
}
