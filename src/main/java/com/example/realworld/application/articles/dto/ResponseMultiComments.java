package com.example.realworld.application.articles.dto;

import com.example.realworld.application.articles.persistence.Comment;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@ToString
public class ResponseMultiComments {

    private final Set<ResponseSingleComment> comments;

    private final Integer commentSize;

    private ResponseMultiComments(Set<ResponseSingleComment> comments) {
        this.comments = comments;
        this.commentSize = comments.size();
    }

    public static ResponseMultiComments from(Set<Comment> comments) {
        Set<ResponseSingleComment> responseSingleComments = comments.stream()
                .map(ResponseSingleComment::from)
                .collect(Collectors.toUnmodifiableSet());
        return new ResponseMultiComments(responseSingleComments);
    }
}
