package com.example.realworld.application.favorites.service;

import com.example.realworld.application.articles.dto.ResponseArticle;
import com.example.realworld.application.articles.exception.DuplicateFavoriteArticleException;
import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.articles.service.ArticleDomainService;
import com.example.realworld.application.favorites.persistence.FavoriteArticle;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.application.users.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoriteArticleBusinessService implements FavoriteArticleService {

    private final UserDomainService userDomainService;
    private final ArticleDomainService articleDomainService;
    private final FavoriteArticleDomainService favoriteDomainService;

    /**
     * 현재 사용자의 관심 글 등록
     *
     * @param email 현재 사용자의 이메일 정보
     * @param slug  해당 글의 Slug 정보
     * @return 현재 사용자가 특정 글에 대해 관심 글로 등록
     */
    @Transactional
    @Override
    public ResponseArticle favoriteArticle(String email, String slug) {

        User findUser = userDomainService.findUserByEmail(email);
        boolean existsFavorite = findUser.isMatchesArticleBySlug(slug);

        if (existsFavorite) {
            throw new DuplicateFavoriteArticleException("이미 좋아요 누른 글입니다.");
        }

        Article findArticle = articleDomainService.getArticleOrElseThrow(slug);
        FavoriteArticle savedFavoriteArticle = favoriteDomainService.save(findUser, findArticle);
        Article resultArticle = findUser.favArticle(savedFavoriteArticle);

        return ResponseArticle.of(resultArticle, findUser);
    }

    /**
     * 현재 사용자의 관심 글 취소
     *
     * @param email 현재 사용자의 이메일 정보
     * @param slug  해당 글의 Slug 정보
     * @return 현재 사용자가 글 정보에 대해서 좋아요 처리를 취소한 정보를 반환
     */
    @Transactional
    @Override
    public ResponseArticle unFavoriteArticle(String email, String slug) {
        // 존재하는 사용자와 글이 있으며, 좋아요 관계가 성립되는 경우
        User findUser = userDomainService.findUserByEmail(email);

        FavoriteArticle favoriteArticle = findUser.getFavArticle(slug);
        Article resultArticle = findUser.unFavArticle(favoriteArticle);

        favoriteDomainService.delete(favoriteArticle);

        return ResponseArticle.of(resultArticle, findUser);
    }
}
