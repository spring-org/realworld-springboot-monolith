package com.example.realworld.application.articles.repository;

import com.example.realworld.application.articles.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}