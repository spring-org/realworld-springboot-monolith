package com.example.realworld.application.articles.persistence;

import com.example.realworld.application.articles.exception.NotFoundCommentException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleComments {

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, orphanRemoval = true, cascade = {PERSIST, REMOVE})
    @ToString.Exclude
    private final Set<Comment> comments = new HashSet<>();

    public static ArticleComments init() {
        return new ArticleComments();
    }

    public Set<Comment> all() {
        return comments;
    }

    public void add(Comment comment) {
        this.comments.add(comment);
    }

    public void addAll(List<Comment> comments) {
        this.comments.addAll(comments);
    }

    public Comment get(Long commentId) {
        return this.comments.stream()
                .filter(comment -> comment.isMatches(commentId))
                .findFirst()
                .orElseThrow(NotFoundCommentException::new);
    }

    public void remove(Comment savedComment) {
        this.comments.remove(savedComment);
    }

    public int size() {
        return comments.size();
    }
}
