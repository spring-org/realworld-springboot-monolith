package com.example.realworld.core.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
@JsonTypeName(value = "errors")
public class ResponseData {
    @NonNull
    @JsonProperty(value = "body")
    private final String message;

    public ResponseData(@NonNull String message) {
        this.message = message;
    }

    public static ResponseData from(String message) {
        return new ResponseData(message);
    }
}