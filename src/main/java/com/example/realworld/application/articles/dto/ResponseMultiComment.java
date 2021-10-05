package com.example.realworld.application.articles.dto;

import com.example.realworld.application.articles.persistence.Comment;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonPropertyOrder({"comments", "commentSize"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMultiComment {

    @JsonUnwrapped
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
