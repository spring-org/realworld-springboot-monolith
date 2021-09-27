package com.example.realworld.application.favorites.persistence;

import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.core.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

/**
 * User와 Article의 Favorite 관계를 영속화하는 엔티티
 */
@Getter
@ToString
@Entity(name = "favoriteArticles")
@Table(name = "TB_FAVORITE_ARTICLE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteArticle extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "FAVORITE_ARTICLE_ID", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User favoriteUser;

    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID")
    private Article favoritedArticle;

    private FavoriteArticle(User fromUser, Article favArticle) {
        this.favoriteUser = fromUser;
        this.favoritedArticle = favArticle;
    }

    public static FavoriteArticle of(User fromUser, Article article) {
        return new FavoriteArticle(fromUser, article);
    }

    public boolean isMatchesArticleBySlug(String slug) {
        return favoritedArticle.isSlugMatches(slug);
    }

    public boolean isMatchesUser(User favUser) {
        return this.favoriteUser.equals(favUser);
    }

    public User user() {
        return favoriteUser;
    }

    public Article article() {
        return favoritedArticle;
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoriteArticle)) return false;
        FavoriteArticle that = (FavoriteArticle) o;
        return Objects.equals(id, that.id)
                && Objects.equals(favoriteUser, that.favoriteUser)
                && Objects.equals(favoritedArticle, that.favoritedArticle);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(id, favoriteUser, favoritedArticle);
    }
}
