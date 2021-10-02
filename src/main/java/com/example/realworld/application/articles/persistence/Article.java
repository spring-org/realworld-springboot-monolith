package com.example.realworld.application.articles.persistence;

import com.example.realworld.application.articles.exception.NotFoundCommentException;
import com.example.realworld.application.favorites.persistence.FavoriteArticle;
import com.example.realworld.application.tags.persistence.TagType;
import com.example.realworld.application.users.persistence.User;
import com.example.realworld.core.persistence.BaseTimeEntity;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;

@Getter
@ToString
@Table(name = "TB_ARTICLE")
@Entity(name = "articles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private boolean favorited;

    private Integer favoritesCount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User author;

    @OneToMany(mappedBy = "favoritedArticle", fetch = FetchType.LAZY, orphanRemoval = true, cascade = {PERSIST, REMOVE})
    @ToString.Exclude
    private final Set<FavoriteArticle> favoriteUser = new HashSet<>();

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, orphanRemoval = true, cascade = {PERSIST, REMOVE})
    @ToString.Exclude
    private final Set<Comment> comments = new HashSet<>();

    @ElementCollection(targetClass = TagType.class)
    @CollectionTable
    @Enumerated(EnumType.STRING)
    private final Set<TagType> tags = new HashSet<>();

    private Article(String title, String description, String body, Set<TagType> tags, User author) {
        this(title, description, body, false, 0, tags, author);
    }

    private Article(
            String title, String description, String body,
            boolean favorited, Integer favoritesCount, Set<TagType> tags, User author) {
        this.slug = makeSlug(title);
        this.title = title;
        this.description = description;
        this.body = body;
        this.favorited = favorited;
        this.favoritesCount = favoritesCount;
        this.author = author;
        this.tags.addAll(tags);
    }

    public static Article of(
            String title, String description, String body, Set<TagType> tags, User author) {
        return new Article(title, description, body, tags, author);
    }

    private String makeSlug(String title) {
        return String.format("%s-%s", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), title);
    }

    // ========================================== Article
    public String author() {
        return author.getProfile().userName();
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

    // ========================================== Comment
    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void addComments(List<Comment> comments) {
        this.comments.addAll(comments);
    }

    public Comment getComments(Long commentId) {
        return this.comments.stream()
                .filter(comment -> comment.isMatches(commentId))
                .findFirst()
                .orElseThrow(() -> new NotFoundCommentException("존재하지 않는 커멘트입니다."));
    }

    public boolean isMatches(String title) {
        return this.title.equals(title);
    }

    public void removeComment(Comment savedComment) {
        this.comments.remove(savedComment);
    }

    // ========================================== Tag

    public boolean isSlugMatches(String slug) {
        return this.slug.equals(slug);
    }

    // ========================================== Favorite
    public Integer getFavUserCount() {
        return favoriteUser.size();
    }

    public Article addFavArticle(FavoriteArticle favArticle) {
        this.favoriteUser.add(favArticle);
        return updateFavFlag(favArticle.user());
    }

    public Article removeFavArticle(FavoriteArticle favArticle) {
        this.favoriteUser.remove(favArticle);
        return updateFavFlag(favArticle.user());
    }

    private Article updateFavFlag(User favoriteUser) {
        favorited = this.favoriteUser.stream()
                .anyMatch(favoriteArticle -> favoriteArticle.isMatchesUser(favoriteUser));
        return this;
    }

    public boolean containsFavUser(User favoriteUser) {
        return this.favoriteUser.stream()
                .anyMatch(favoriteArticle -> favoriteArticle.isMatchesUser(favoriteUser));
    }

    // jacoco 라이브러리가 lobok 에서 생성된 메서드를 무시할 수 있도록 설정하기 위한 어노테이션
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
