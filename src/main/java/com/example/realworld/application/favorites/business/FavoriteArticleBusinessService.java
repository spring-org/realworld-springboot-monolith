package com.example.realworld.application.favorites.business;

import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.articles.persistence.ArticleDomainService;
import com.example.realworld.application.articles.dto.ResponseArticle;
import com.example.realworld.application.articles.exception.DuplicateFavoriteArticleException;
import com.example.realworld.application.favorites.persistence.FavoriteArticle;
import com.example.realworld.application.favorites.exception.NotYetFavoriteArticleException;
import com.example.realworld.application.favorites.persistence.repository.FavoriteArticleRepository;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.application.users.persistence.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoriteArticleBusinessService implements FavoriteArticleService {

    private final UserDomainService userDomainService;
    private final FavoriteArticleRepository favoriteArticleRepository;
    private final ArticleDomainService articleDomainService;

    @Transactional
    @Override
    public ResponseArticle favoriteArticle(String email, String slug) {

        User findUser = userDomainService.findUserByEmail(email);
        Article findArticle = articleDomainService.getArticleOrElseThrow(slug);

        boolean existsFavorite = findUser.isMatchesArticleBySlug(slug);

        if (existsFavorite) {
            throw new DuplicateFavoriteArticleException("이미 좋아요 누른 글입니다.");
        }

        FavoriteArticle savedFavoriteArticle = favoriteArticleRepository.save(FavoriteArticle.of(findUser, findArticle));
        Article resultArticle = findUser.favArticle(savedFavoriteArticle);

        return ResponseArticle.of(resultArticle, findUser);
    }

    @Transactional
    @Override
    public ResponseArticle unFavoriteArticle(String email, String slug) {
        // 존재하는 사용자와 글이 있으며, 좋아요 관계가 성립되는 경우
        User findUser = userDomainService.findUserByEmail(email);

        boolean existsFavorite = findUser.isMatchesArticleBySlug(slug);

        if (!existsFavorite) {
            throw new NotYetFavoriteArticleException("좋아요 누른 글이 아닙니다.");
        }

        FavoriteArticle alreadyFavoriteArticle = findUser.getFavArticle(slug);
        Article resultArticle = findUser.unFavArticle(alreadyFavoriteArticle);
        favoriteArticleRepository.delete(alreadyFavoriteArticle);

        return ResponseArticle.of(resultArticle, findUser);
    }
}
