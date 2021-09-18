package com.example.realworld.application.users.repository;

import com.example.realworld.application.users.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
