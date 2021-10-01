package com.example.realworld.application.articles.dto;

import com.example.realworld.application.articles.persistence.Comment;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@ToString
@JsonPropertyOrder({"comments", "commentSize"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMultiComment {

    private final Set<ResponseSingleComment> comments;
    private final Integer commentSize;

    private ResponseMultiComment(Set<ResponseSingleComment> comments) {
        this.comments = comments;
        this.commentSize = comments.size();
    }

    public static ResponseMultiComment from(Set<Comment> comments) {
        final Set<ResponseSingleComment> responseSingleComments = comments.stream()
                .map(ResponseSingleComment::from)
                .collect(Collectors.toUnmodifiableSet());
        return new ResponseMultiComment(responseSingleComments);
    }
}
