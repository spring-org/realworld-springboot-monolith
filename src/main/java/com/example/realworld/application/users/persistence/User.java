package com.example.realworld.application.users.persistence;

import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.favorites.exception.NotFoundFavoriteArticleException;
import com.example.realworld.application.favorites.persistence.FavoriteArticle;
import com.example.realworld.application.follows.persistence.Follow;
import com.example.realworld.application.users.dto.RequestUpdateUser;
import com.example.realworld.core.persistence.BaseTimeEntity;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import static javax.persistence.CascadeType.*;

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
    @LastModifiedDate
    private LocalDateTime updatedAt;
    private String token;
    @Embedded
    private Profile profile;
    @Embedded
    private Follows follows;

    @OneToMany(mappedBy = "author", orphanRemoval = true, cascade = {PERSIST, MERGE})
    @ToString.Exclude
    private final Set<Article> articles = new HashSet<>();

    @OneToMany(mappedBy = "favoriteUser", orphanRemoval = true, cascade = {PERSIST, MERGE})
    @ToString.Exclude
    private final Set<FavoriteArticle> favoriteArticles = new HashSet<>();

    private User(String email, String password) {
        this(email, password, new Profile(), new Follows(), null);
    }

    private User(String email, String password, Profile profile, Follows follows, String token) {
        this.email = email;
        this.password = password;
        this.profile = profile;
        this.token = token;
        this.follows = follows;
    }

    // ========================================== User
    public static User of(String email, String password) {
        return new User(email, password);
    }

    public static User of(String email, String password, String userName) {
        return new User(email, password, Profile.from(userName), Follows.init(), null);
    }

    public boolean isSameUser(User toUser) {
        return this.getEmail().equals(toUser.getEmail());
    }
    // ========================================== profile

    public Profile profile() {
        return profile;
    }

    public String userName() {
        return this.profile.userName();
    }

    public User generateToken(String generateToken) {
        this.token = generateToken;
        return this;
    }

    // 프로필 업데이트
    public void update(RequestUpdateUser updateUser) {
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

    // ========================================== Follow
    public Follows follows() {
        return follows;
    }

    // 팔로우 추가
    public void following(Follow newFollow) {
        follows.addFollowing(newFollow);
    }

    public void followers(Follow newFollow) {
        follows.addFollowers(newFollow);
        updateFollowFlag(newFollow.toUser());
    }

    // 언팔
    public void unFollowing(Follow newFollow) {
        follows.removeFollowing(newFollow);
    }

    public void unFollowers(Follow newFollow) {
        follows.removeFollowers(newFollow);
        updateFollowFlag(newFollow.toUser());
    }

    private void updateFollowFlag(User toUser) {
        boolean anyMatch = follows.isFollowers(toUser);
        this.profile.changeFollowing(anyMatch);
    }

    // 팔로우 관계인지 확인
    public boolean isFollowing(User toUser) {
        return follows.isFollowing(toUser);
    }

    public boolean isFollowers(User toUser) {
        return follows.isFollowers(toUser);
    }

    // 팔로우 관계를 찾기위한 검색
    public Follow findFollowing(User toUser) {
        return follows.findFollowing(toUser);
    }

    // ========================================== Article
    // 글 다중 등록
    public void addArticles(List<Article> articles) {
        this.articles.addAll(articles);
    }

    // 글 추가
    public void addArticle(Article article) {
        this.articles.add(article);
    }

    // 글 삭제
    public void removeArticle(Article findArticle) {
        this.articles.remove(findArticle);
    }

    // 글 타이틀로 조회
    public Optional<Article> getArticleByTitle(String title) {
        return this.articles.stream()
                .filter(article -> article.isMatches(title))
                .findFirst();
    }

    // 글 Slug로 조회
    public Optional<Article> getArticleBySlug(String slug) {
        return this.articles.stream()
                .filter(article -> article.isSlugMatches(slug))
                .findFirst();
    }

    // ========================================== Favorite
    // 글 좋아요 처리
    public Article favArticle(FavoriteArticle newFavArticle) {
        this.favoriteArticles.add(newFavArticle);
        Article article = newFavArticle.article();
        return article.addFavArticle(newFavArticle);
    }

    // 글 좋아요 취소 처리
    public Article unFavArticle(FavoriteArticle favoriteArticle) {
        this.favoriteArticles.remove(favoriteArticle);
        Article article = favoriteArticle.article();
        return article.removeFavArticle(favoriteArticle);
    }

    // 관심 글 확인
    public boolean isMatchesArticleBySlug(String slug) {
        return this.favoriteArticles.stream()
                .anyMatch(favArticle -> favArticle.isMatchesArticleBySlug(slug));
    }

    // 관심 글 Slug로 검색
    public FavoriteArticle getFavArticle(String slug) {
        return this.favoriteArticles.stream()
                .filter(favArticle -> favArticle.isMatchesArticleBySlug(slug))
                .findFirst()
                .orElseThrow(NotFoundFavoriteArticleException::new);
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
