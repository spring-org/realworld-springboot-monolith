package com.example.realworld.application.articles;

import com.example.realworld.application.articles.dto.RequestSaveArticle;
import com.example.realworld.application.articles.dto.RequestUpdateArticle;

public class ArticleFixture {
    private ArticleFixture() {
    }

    public static RequestSaveArticle getRequestSaveArticle(String title, String[] tagList) {
        return RequestSaveArticle.of(title, "설명", "내용", tagList);
    }

    public static RequestUpdateArticle getRequestUpdateArticle(String updatedTitle, String updatedDescription) {
        return RequestUpdateArticle.of(updatedTitle, updatedDescription, "내용");
    }
}
