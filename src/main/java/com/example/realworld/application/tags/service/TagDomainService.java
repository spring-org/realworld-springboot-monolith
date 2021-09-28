package com.example.realworld.application.tags.service;

import com.example.realworld.application.tags.persistence.Tag;
import com.example.realworld.application.tags.persistence.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagDomainService {

    private final TagRepository tagRepository;

    /**
     * 전체 태그 정보 조회
     *
     * @return 전체 태그 정보 반환
     */
    @Transactional(readOnly = true)
    public List<Tag> findTagByAll() {
        return tagRepository.findAll();
    }
}
