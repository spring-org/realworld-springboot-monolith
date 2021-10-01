package com.example.realworld.application.tags.persistence;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum TagType {

    JAVA("Java"),
    JAVASCRIPT("JavaScript"),
    PYTHON("Python"),
    PERL("Perl"),
    PHP("PHP"),
    CPP("C++"),
    KOTLIN("Kotlin"),
    R("R"),
    RUBY("Ruby"),
    EMPTY(null);

    private final String tagName;

    private static final Map<String, TagType> tagTypeMap =
            Collections.unmodifiableMap(Arrays.stream(values())
                    .collect(Collectors.toMap(TagType::tagName, Function.identity())));

    TagType(String tagName) {
        this.tagName = tagName;
    }

    public static String from(String tagName) {
        return Optional.ofNullable(tagTypeMap.get(tagName))
                .orElse(EMPTY).tagName;
    }

    public static TagType of(String tagName) {
        return Optional.ofNullable(tagTypeMap.get(tagName)).orElse(null);
    }

    public static List<TagType> all() {
        return List.of(JAVA, JAVASCRIPT, PYTHON, PERL, PHP, CPP, KOTLIN, R, RUBY);
    }

    public String tagName() {
        return tagName;
    }
}
