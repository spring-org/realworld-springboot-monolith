package com.example.realworld.application.articles.persistence.repository;

import com.example.realworld.application.articles.persistence.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}