package com.example.realworld.application.users.persistence;

import com.example.realworld.application.users.exception.NotFoundUserException;
import com.example.realworld.application.users.persistence.repository.UserRepository;
import com.example.realworld.core.annotations.DomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@RequiredArgsConstructor
public class UserDomainService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User findUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundUserException("존재하지 않는 사용자입니다."));
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String userEmail) {
        return userRepository.existsByEmail(userEmail);
    }

    @Transactional
    public User save(User userEntity) {
        return userRepository.save(userEntity);
    }
}
