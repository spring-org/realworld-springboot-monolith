package com.example.realworld.application.articles.service;

import com.example.realworld.application.articles.dto.RequestSaveArticle;
import com.example.realworld.application.articles.exception.NotFoundArticleException;
import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.articles.persistence.repository.ArticleRepository;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.core.annotations.DomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@RequiredArgsConstructor
public class ArticleDomainService {

    private final ArticleRepository articleRepository;

    /**
     * 특정 글을 조회 및 예외 처리
     *
     * @param slug 특정 글의 Slug
     * @return 특정 글 조회
     */
    @Transactional(readOnly = true)
    public Article getArticleBySlug(String slug) {
        return articleRepository.findBySlugOrderByIdDesc(slug)
                .orElseThrow(NotFoundArticleException::new);
    }

    /**
     * 새로운 글 등록 처리
     *
     * @param saveArticle 새로운 글을 등록하기 위한 정보
     * @param author      현재 사용자의 정보
     * @return 새로 등록된 글 반환
     */
    @Transactional
    public Article addArticle(RequestSaveArticle saveArticle, User author) {

        Article article = RequestSaveArticle.toEntity(saveArticle, author);

        return articleRepository.save(article);
    }
}
