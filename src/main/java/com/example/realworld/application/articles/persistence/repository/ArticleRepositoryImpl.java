package com.example.realworld.application.articles.persistence.repository;

import com.example.realworld.application.articles.dto.RequestArticleCondition;
import com.example.realworld.application.articles.persistence.Article;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.example.realworld.application.articles.persistence.QArticle.article;
import static com.example.realworld.application.follows.persistence.QFollow.follow;
import static com.example.realworld.application.tags.persistence.QTag.tag;
import static com.example.realworld.application.users.persistence.QUser.user;

public class ArticleRepositoryImpl implements ArticleQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public ArticleRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Article> searchPageArticle(RequestArticleCondition condition, Pageable pageable) { // string

        JPAQuery<Article> query = getQuery(condition, pageable);

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder<Article> pathBuilder = new PathBuilder<>(article.getType(), article.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty())));
        }

        List<Article> contents = query.fetch();

        JPAQuery<Article> countQuery = getQuery(condition, pageable);

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchCount);
    }

    private JPAQuery<Article> getQuery(RequestArticleCondition condition, Pageable pageable) {
        return queryFactory
                .select(article)
                .from(article)
                .innerJoin(article.author, user)
                .innerJoin(article.tags, tag)
                .where(
                        condition(condition.getTag(), article.tags.any().name::eq),
                        condition(condition.getAuthor(), article.author.email::eq),
                        condition(condition.getFavorited(), user.follows.followers.any().toUser.email::eq)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
    }

    @Override
    public List<Article> searchPageFeed(String email, Pageable pageable) {
        return queryFactory
                .select(article)
                .from(user)
                .innerJoin(follow).on(follow.fromUser.id.eq(user.id))
                .innerJoin(follow).on(follow.toUser.id.eq(article.author.id))
                .innerJoin(article).on(article.author.id.eq(follow.toUser.id))
                .orderBy(article.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private <T> BooleanExpression condition(T value, Function<T, BooleanExpression> function) {
        return Optional.ofNullable(value).map(function).orElse(null);
    }

}
