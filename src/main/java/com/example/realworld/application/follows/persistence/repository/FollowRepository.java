package com.example.realworld.application.follows.persistence.repository;

import com.example.realworld.application.follows.persistence.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
