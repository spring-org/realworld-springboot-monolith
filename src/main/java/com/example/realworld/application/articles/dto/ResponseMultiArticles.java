package com.example.realworld.application.articles.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ResponseMultiArticles {

    private final List<ResponseArticle> articles;
    private final int articleCount;

    private ResponseMultiArticles(List<ResponseArticle> articles, int articleCount) {
        this.articles = articles;
        this.articleCount = articleCount;
    }

    public static ResponseMultiArticles of(List<ResponseArticle> articles) {
        return new ResponseMultiArticles(articles, articles.size());
    }
}
