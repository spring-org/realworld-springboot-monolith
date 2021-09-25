package com.example.realworld.application.follows.repository;

import com.example.realworld.application.follows.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
