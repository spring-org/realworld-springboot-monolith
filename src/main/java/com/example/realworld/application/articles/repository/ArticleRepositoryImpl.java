package com.example.realworld.application.articles.repository;

import com.example.realworld.application.articles.domain.Article;
import com.example.realworld.application.articles.dto.RequestPageCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.realworld.application.articles.domain.QArticle.article;

public class ArticleRepositoryImpl implements ArticleQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public ArticleRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Article> searchPageArticle(RequestPageCondition condition) {
        BooleanBuilder builder = new BooleanBuilder();
        // TODO 확인 필요..
//        if (StringUtils.hasText(condition.getTag())) {
//            builder.and(article.tags.contains(Tag.of(condition.getTag())));
//        }
        if (StringUtils.hasText(condition.getAuthor())) {
            builder.and(article.author.email.eq(condition.getAuthor()));
        }
        if (StringUtils.hasText(condition.getFavorited())) {
            builder.and(article.author.profile.userName.eq(condition.getFavorited()));
        }
        return queryFactory
                .selectFrom(article)
                .where(builder)
                .offset(condition.getOffset())
                .limit(condition.getLimit())
                .fetch();
    }
}
