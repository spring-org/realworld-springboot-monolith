package com.example.realworld.application.articles;

import com.example.realworld.application.articles.dto.RequestSaveArticle;
import com.example.realworld.application.articles.dto.RequestUpdateArticle;
import com.example.realworld.application.articles.persistence.Article;
import com.example.realworld.application.tags.persistence.Tag;
import com.example.realworld.application.users.persistence.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ArticleFixture {

    private ArticleFixture() {
    }

    public static Article createArticle(Integer idx, User author, Tag... tags) {
        return Article.of("타이틀-" + idx, "description", "body", author, tags);
    }

    public static String makeSlug(String title) {
        return String.format(
                "%s-%s"
                , LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                , title);
    }

    public static RequestSaveArticle getRequestSaveArticle(int idx, String... tags) {
        return RequestSaveArticle.of("타이틀-" + idx, "설명", "내용", tags);
    }

    public static RequestSaveArticle getRequestDeletedArticle(String title, String... tags) {
        return RequestSaveArticle.of(title, "설명", "내용", tags);
    }

    public static RequestUpdateArticle getRequestUpdateArticle(String title, String description, String body) {
        return RequestUpdateArticle.of(title, description, body);
    }

}
