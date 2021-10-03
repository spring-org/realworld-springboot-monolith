package com.example.realworld.application.articles.persistence;

import com.example.realworld.application.favorites.persistence.FavoriteArticle;
import com.example.realworld.application.users.persistence.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteArticles {

    @OneToMany(mappedBy = "favoritedArticle", fetch = FetchType.LAZY, orphanRemoval = true, cascade = {PERSIST, REMOVE})
    @ToString.Exclude
    private final Set<FavoriteArticle> favArticles = new HashSet<>();

    private boolean favorited;

    private FavoriteArticles(boolean favorited) {
        this.favorited = favorited;
    }

    public static FavoriteArticles init() {
        return new FavoriteArticles(false);
    }

    public int size() {
        return favArticles.size();
    }

    public void add(FavoriteArticle favArticle) {
        this.favArticles.add(favArticle);
        updateFavFlag(favArticle.user());
    }

    public void remove(FavoriteArticle favArticle) {
        this.favArticles.remove(favArticle);
        updateFavFlag(favArticle.user());
    }

    public void updateFavFlag(User favUser) {
        this.favorited = this.favArticles.stream()
                .anyMatch(favoriteArticle -> favoriteArticle.isMatchesUser(favUser));
    }

    public boolean contains(User favUser) {
        return this.favArticles.stream()
                .anyMatch(favoriteArticle -> favoriteArticle.isMatchesUser(favUser));
    }

}
