package com.example.realworld.application.articles.persistence;

import com.example.realworld.application.favorites.persistence.FavoriteArticle;
import com.example.realworld.application.tags.persistence.Tag;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.core.persistence.BaseTimeEntity;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.CascadeType.PERSIST;

@Getter
@ToString
@Table(name = "TB_ARTICLE",
		indexes = {
			@Index(name = "idx_article_title", columnList = "title")
			, @Index(name = "idx_article_author", columnList = "USER_ID")
			, @Index(name = "idx_article_updated_at", columnList = "updatedAt")
			, @Index(name = "idx_article_created_at", columnList = "CREATED_AT")
		}
)
@Entity(name = "articles")
public class Article extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ARTICLE_ID", nullable = false)
    private Long id;
    private String slug;
    private String title;
    private String description;
    private String body;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @Embedded
    private FavoriteArticles favoriteArticles;
    @Embedded
    private ArticleComments comments;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User author;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {PERSIST})
    private final Set<Tag> tags = new LinkedHashSet<>();

	protected Article() {}

	private Article(
            String title, String description, String body, User author, Tag... tags) {
        this.slug = makeSlug(title);
        this.title = title;
        this.description = description;
        this.body = body;
        this.author = author;
        this.tags.addAll(List.of(tags));
        this.favoriteArticles = FavoriteArticles.init();
        this.comments = ArticleComments.init();
    }

    public static Article of(
            String title, String description, String body, User author, Tag... tags) {
        return new Article(title, description, body, author, tags);
    }

    private String makeSlug(String title) {
        return String.format("%s-%s", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), title);
    }

    // ========================================== Article
    public String author() {
        return author.userName();
    }

    public ArticleComments comments() {
        return comments;
    }

    public boolean isMatches(String title) {
        return this.title.equals(title);
    }

    public boolean isSlugMatches(String slug) {
        return this.slug.equals(slug);
    }

    public void update(String title, String description, String body) {
        if (StringUtils.hasText(title)) {
            this.title = title;
            this.slug = makeSlug(title);
        }
        if (StringUtils.hasText(description)) {
            this.description = description;
        }
        if (StringUtils.hasText(body)) {
            this.body = body;
        }
    }
    // ========================================== Tag

    public Set<Tag> tags() {
        return tags;
    }

    // ========================================== Favorite
    public Article addFavArticle(FavoriteArticle favArticle) {
        this.favoriteArticles.add(favArticle);
        return this;
    }

    public Article removeFavArticle(FavoriteArticle favArticle) {
        this.favoriteArticles.remove(favArticle);
        return this;
    }

    // jacoco 라이브러리가 lombok 에서 생성된 메서드를 무시할 수 있도록 설정하기 위한 어노테이션
    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.id);
    }

    // jacoco 라이브러리가 lobok 에서 생성된 메서드를 무시할 수 있도록 설정하기 위한 어노테이션
    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
