package com.example.realworld.application.articles.dto;

import com.example.realworld.application.articles.persistence.Article;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@JsonPropertyOrder({"articles", "articleCount"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMultiArticle {

    @JsonUnwrapped
    private final List<ResponseSingleArticle> articles;
    private final int articleCount;

    private ResponseMultiArticle(List<ResponseSingleArticle> articles, int articleCount) {
        this.articles = articles;
        this.articleCount = articleCount;
    }

    public static ResponseMultiArticle of(final List<Article> articles) {
        final List<ResponseSingleArticle> responseArticles = articles.stream()
                .map(ResponseSingleArticle::from)
                .collect(Collectors.toList());
        return new ResponseMultiArticle(responseArticles, articles.size());
    }
}
