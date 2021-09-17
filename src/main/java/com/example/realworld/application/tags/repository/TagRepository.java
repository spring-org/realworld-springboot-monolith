package com.example.realworld.application.tags.repository;

import com.example.realworld.application.tags.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
