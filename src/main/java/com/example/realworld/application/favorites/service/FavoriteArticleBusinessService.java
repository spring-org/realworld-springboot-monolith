package com.example.realworld.application.favorites.service;

import com.example.realworld.application.articles.domain.Article;
import com.example.realworld.application.articles.dto.ResponseArticle;
import com.example.realworld.application.articles.exception.DuplicateFavoriteArticleException;
import com.example.realworld.application.articles.exception.NotFoundArticleException;
import com.example.realworld.application.articles.repository.ArticleRepository;
import com.example.realworld.application.favorites.domain.FavoriteArticle;
import com.example.realworld.application.favorites.exception.NotFoundFavoriteArticleException;
import com.example.realworld.application.favorites.repository.FavoriteArticleRepository;
import com.example.realworld.application.users.domain.User;
import com.example.realworld.application.users.exception.NotFoundUserException;
import com.example.realworld.application.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoriteArticleBusinessService implements FavoriteArticleService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final FavoriteArticleRepository favoriteArticleRepository;


    @Transactional
    @Override
    public ResponseArticle favoriteArticle(String email, String slug) {

        User findUser = findUser(email);
        Article findArticle = findArticleByEmail(slug);

        boolean existsFavorite = findUser.isMatchesArticle(slug);

        if (existsFavorite) {
            throw new DuplicateFavoriteArticleException("이미 Favorite 관계이다.");
        }

        FavoriteArticle savedFavoriteArticle = favoriteArticleRepository.save(FavoriteArticle.of(findUser, findArticle));
        Article resultArticle = findUser.favoriteArticle(savedFavoriteArticle);

        return ResponseArticle.of(resultArticle, findUser);
    }

    @Override
    public ResponseArticle unFavoriteArticle(String email, String slug) {
        // 존재하는 사용자와 글이 있으며, 좋아요 관계가 성립되는 경우
        User findUser = findUser(email);

        boolean existsFavorite = findUser.isMatchesArticle(slug);

        if (!existsFavorite) {
            throw new DuplicateFavoriteArticleException("Favorite 관계가 아닙니다.");
        }

        FavoriteArticle alreadyFavoriteArticle = findUser.getFavoriteArticles().stream()
                .filter(favoriteArticle -> favoriteArticle.isMatchesArticleBySlug(slug))
                .findFirst()
                .orElseThrow(() -> new NotFoundFavoriteArticleException("Favorite 관계에 대한 정보가 존재하지 않습니다."));

        Article resultArticle = findUser.unFavoriteArticle(alreadyFavoriteArticle);

        return ResponseArticle.of(resultArticle, findUser);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundUserException("존재하지 않는 사용자 입니다."));
    }

    private Article findArticleByEmail(String slug) {
        return articleRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundArticleException("존재하지 않는 팔로우 관계입니다."));
    }
}
