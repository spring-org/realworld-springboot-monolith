package com.example.realworld.application.articles.persistence;

import com.example.realworld.application.articles.exception.NotFoundArticleException;
import com.example.realworld.application.articles.persistence.repository.ArticleRepository;
import com.example.realworld.core.annotations.DomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@RequiredArgsConstructor
public class ArticleDomainService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Article getArticleOrElseThrow(String slug) {
        return articleRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundArticleException("존재하지 않는 글입니다."));
    }
}
