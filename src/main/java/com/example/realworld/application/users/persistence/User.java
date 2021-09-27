package com.example.realworld.application.users.persistence;

import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.articles.exception.NotFoundArticleException;
import com.example.realworld.application.favorites.persistence.FavoriteArticle;
import com.example.realworld.application.favorites.exception.NotFoundFavoriteArticleException;
import com.example.realworld.application.follows.persistence.Follow;
import com.example.realworld.application.follows.exception.NotFoundFollowException;
import com.example.realworld.application.users.dto.RequestUpdateUser;
import com.example.realworld.core.domain.BaseTimeEntity;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;

@Getter
@ToString
@Table(name = "TB_USER")
@Entity(name = "users")
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

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private String token;

    @OneToMany(mappedBy = "fromUser", fetch = FetchType.LAZY, orphanRemoval = true, cascade = {PERSIST, REMOVE})
    @ToString.Exclude
    private final Set<Follow> following = new HashSet<>();

    @OneToMany(mappedBy = "toUser", fetch = FetchType.LAZY)
    @ToString.Exclude
    private final Set<Follow> followers = new HashSet<>();

    @OneToMany(mappedBy = "author", orphanRemoval = true, cascade = {PERSIST, REMOVE})
    @ToString.Exclude
    private final Set<Article> articles = new HashSet<>();

    @OneToMany(mappedBy = "favoriteUser", orphanRemoval = true, cascade = {PERSIST, REMOVE})
    @ToString.Exclude
    private final Set<FavoriteArticle> favoriteArticles = new HashSet<>();

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
    public void update(RequestUpdateUser updateUser) {
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
    }

    // Follow
    public void follow(Follow newFollow) {
        this.following.add(newFollow);
    }

    public void unFollow(Follow findFollow) {
        this.following.remove(findFollow);
    }

    public boolean isFollowing(User toUser) {
        return this.following.stream()
                .anyMatch(follow -> follow.isSameToUser(toUser));
    }

    public Follow findFollowing(User toUser) {
        return this.getFollowing()
                .stream()
                .filter(follow -> follow.isSameToUser(toUser))
                .findAny()
                .orElseThrow(() -> new NotFoundFollowException("사용자 간의 팔로우 관계가 존재하지 않습니다."));
    }

    // Article
    public void addArticles(List<Article> articles) {
        this.articles.addAll(articles);
    }

    public void addArticle(Article article) {
        this.articles.add(article);
    }

    public void removeArticle(Article findArticle) {
        this.articles.remove(findArticle);
    }

    public Article getArticleByTitle(String title) {
        return this.articles.stream()
                .filter(article -> article.isMatches(title))
                .findFirst()
                .orElseThrow(() -> new NotFoundArticleException("존재하지 않는 글입니다."));
    }

    public Article getArticleBySlug(String slug) {
        return this.articles.stream()
                .filter(article -> article.isSlugMatches(slug))
                .findFirst()
                .orElseThrow(() -> new NotFoundArticleException("존재하지 않는 글입니다."));
    }

    // Favorite
    public Article favArticle(FavoriteArticle favoriteArticle) {
        this.favoriteArticles.add(favoriteArticle);
        Article article = favoriteArticle.getFavoritedArticle();
        return article.addFavArticle(favoriteArticle);
    }

    public Article unFavArticle(FavoriteArticle favoriteArticle) {
        this.favoriteArticles.remove(favoriteArticle);
        Article article = favoriteArticle.getFavoritedArticle();
        return article.removeFavArticle(favoriteArticle);
    }

    public boolean isMatchesArticleBySlug(String slug) {
        return this.favoriteArticles.stream()
                .anyMatch(favArticle -> favArticle.isMatchesArticleBySlug(slug));
    }

    public FavoriteArticle getFavArticle(String slug) {
        return favoriteArticles.stream()
                .filter(favArticle -> favArticle.isMatchesArticleBySlug(slug))
                .findFirst()
                .orElseThrow(() -> new NotFoundFavoriteArticleException("Favorite 관계에 대한 정보가 존재하지 않습니다."));
    }

    // jacoco 라이브러리가 lobok 에서 생성된 메서드를 무시할 수 있도록 설정하기 위한 어노테이션
    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id)
                && Objects.equals(email, user.email);
    }

    // jacoco 라이브러리가 lobok 에서 생성된 메서드를 무시할 수 있도록 설정하기 위한 어노테이션
    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

}
