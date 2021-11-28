package com.example.realworld.application.favorites.service;

import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.favorites.persistence.FavoriteArticle;
import com.example.realworld.application.favorites.persistence.FavoriteArticleFactory;
import com.example.realworld.application.favorites.persistence.repository.FavoriteArticleRepository;
import com.example.realworld.application.users.persistence.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoriteArticleDomainService {

    private final FavoriteArticleRepository favoriteArticleRepository;

    /**
     * 관심 글에 대한 관계를 형성
     *
     * @param user    현재 사용자
     * @param article 특정 글
     * @return 현재 사용자와 특정 글 간의 관계 형성
     */
    @Transactional
    public FavoriteArticle save(User user, Article article) {

        FavoriteArticle favoriteArticle = FavoriteArticleFactory.of(user, article);

        return favoriteArticleRepository.save(favoriteArticle);
    }

    /**
     * 관심 글 삭제
     *
     * @param favoriteArticle 사용자의 관심 글 관계
     */
    @Transactional
    public void delete(FavoriteArticle favoriteArticle) {
        favoriteArticleRepository.delete(favoriteArticle);
    }
}
