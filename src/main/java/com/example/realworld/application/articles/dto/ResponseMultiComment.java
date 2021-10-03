package com.example.realworld.application.articles.dto;

import com.example.realworld.application.articles.persistence.Comment;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.ToString;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@ToString
@JsonPropertyOrder({"comments", "commentSize"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMultiComment {

    private final List<ResponseSingleComment> comments;
    private final Integer commentSize;

    private ResponseMultiComment(List<ResponseSingleComment> comments) {
        this.comments = comments;
        this.commentSize = comments.size();
    }

    public static ResponseMultiComment from(Set<Comment> comments) {
        final List<ResponseSingleComment> responseSingleComments = comments.stream()
                .sorted(Comparator.comparing(Comment::getId).reversed())
                .map(ResponseSingleComment::from)
                .collect(Collectors.toList());
        return new ResponseMultiComment(responseSingleComments);
    }
}
