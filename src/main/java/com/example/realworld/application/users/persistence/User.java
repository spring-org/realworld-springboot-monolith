package com.example.realworld.application.users.persistence;

import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.favorites.persistence.FavoriteArticle;
import com.example.realworld.application.users.dto.RequestUpdateUser;
import com.example.realworld.core.persistence.BaseTimeEntity;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;

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
    @Column(name = "email")
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

    User(String email, String password) {
        this(email, password, new Profile(), new Follows(), null);
    }

    User(String email, String password, Profile profile, Follows follows, String token) {
        this.email = email;
        this.password = password;
        this.profile = profile;
        this.token = token;
        this.follows = follows;
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
