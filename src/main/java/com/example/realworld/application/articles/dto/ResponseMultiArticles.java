package com.example.realworld.application.articles.dto;

import com.example.realworld.application.articles.domain.Article;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ResponseMultiArticles {

    private final List<ResponseArticle> articles;
    private final int articleCount;

    private ResponseMultiArticles(List<ResponseArticle> articles, int articleCount) {
        this.articles = articles;
        this.articleCount = articleCount;
    }

    public static ResponseMultiArticles of(List<Article> articles) {
        List<ResponseArticle> responseArticles = articles.stream()
                .map(ResponseArticle::of)
                .collect(Collectors.toList());
        return new ResponseMultiArticles(responseArticles, articles.size());
    }
}
