package com.example.realworld.application.users.domain;

import com.example.realworld.application.articles.domain.Article;
import com.example.realworld.application.articles.exception.NotFoundArticleException;
import com.example.realworld.application.users.dto.RequestUpdateUser;
import com.example.realworld.application.users.exception.NotFoundFollowException;
import com.example.realworld.core.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@ToString
@Table(name = "TB_USER")
@Entity(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "USER_ID", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Embedded
    private Profile profile;

    private String token;

    @OneToMany(mappedBy = "fromUser", orphanRemoval = true)
    @ToString.Exclude
    private final Set<Follow> following = new HashSet<>();

    @OneToMany(mappedBy = "toUser")
    @ToString.Exclude
    private final Set<Follow> followers = new HashSet<>();

    @OneToMany(mappedBy = "author", orphanRemoval = true)
    @ToString.Exclude
    private final Set<Article> articles = new HashSet<>();

    private User(String email, String password) {
        this(email, password, new Profile(), null);
    }

    private User(String email, String password, Profile profile, String token) {
        this.email = email;
        this.password = password;
        this.profile = profile;
        this.token = token;
    }

    // User
    public static User of(String email, String password) {
        return new User(email, password);
    }

    public static User of(String email, String password, String userName) {
        return new User(email, password, Profile.from(userName), null);
    }

    public String userName() {
        return this.profile.getUserName();
    }

    public boolean isSameUser(User toUser) {
        return this.getEmail().equals(toUser.getEmail());
    }

    // profile
    public User update(RequestUpdateUser updateUser) {
        if (StringUtils.hasText(updateUser.getEmail())) {
            this.email = updateUser.getEmail();
        }
        if (StringUtils.hasText(updateUser.getPassword())) {
            this.password = updateUser.getPassword();
        }
        if (StringUtils.hasText(updateUser.getUserName())) {
            this.profile.changeUserName(updateUser.getUserName());
        }
        if (StringUtils.hasText(updateUser.getBio())) {
            this.profile.changeBio(updateUser.getBio());
        }
        if (StringUtils.hasText(updateUser.getImage())) {
            this.profile.changeImage(updateUser.getImage());
        }
        return this;
    }

    // Follow
    public void follow(Follow newFollow) {
        if (!isFollowing(newFollow.getToUser())) {
            this.following.add(newFollow);
        }
    }

    public void unFollow(Follow findFollow) {
        this.following.remove(findFollow);
    }

    public boolean isFollowing(User toUser) {
        return this.following.stream()
                .anyMatch(follow -> follow.isSameToUser(toUser));
    }

    public Follow findFollowing(User toUser) {
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
