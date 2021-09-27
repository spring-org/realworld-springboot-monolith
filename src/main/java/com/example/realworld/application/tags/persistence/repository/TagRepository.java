package com.example.realworld.application.tags.persistence.repository;

import com.example.realworld.application.tags.persistence.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
