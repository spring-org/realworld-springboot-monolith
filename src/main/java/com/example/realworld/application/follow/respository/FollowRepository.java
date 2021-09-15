package com.example.realworld.application.follow.respository;

import com.example.realworld.application.follow.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
