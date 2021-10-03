package com.example.realworld.application.articles.persistence.repository;

import com.example.realworld.application.articles.dto.RequestPageCondition;
import com.example.realworld.application.articles.persistence.Article;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.example.realworld.application.articles.persistence.QArticle.article;
import static com.example.realworld.application.follows.persistence.QFollow.follow;
import static com.example.realworld.application.users.persistence.QUser.user;

public class ArticleRepositoryImpl implements ArticleQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public ArticleRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Article> searchPageArticle(RequestPageCondition condition) { // string

        return Collections.unmodifiableList(
                queryFactory
                        .select(article)
                        .from(article)
                        .innerJoin(article.author, user)
                        .where(
                                condition(condition.getTag(), article.tags.any().name::eq),
                                condition(condition.getAuthor(), article.author.email::eq),
                                condition(condition.getFavorited(), user.followers.any().toUser.email::eq)
                        )
                        .offset(condition.getOffset())
                        .limit(condition.getLimit())
                        .fetch()
        );
    }

    @Override
    public List<Article> searchPageFeed(String email, Pageable pageable) {
        return Collections.unmodifiableList(queryFactory
                .select(article)
                .from(user)
                .innerJoin(follow)
                .on(follow.fromUser.id.eq(user.id))
                .innerJoin(follow)
                .on(follow.toUser.id.eq(article.author.id))
                .innerJoin(article)
                .on(article.author.id.eq(follow.toUser.id))
                .orderBy(article.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch());
    }

    private <T> BooleanExpression condition(T value, Function<T, BooleanExpression> function) {
        return Optional.ofNullable(value).map(function).orElse(null);
    }

}
