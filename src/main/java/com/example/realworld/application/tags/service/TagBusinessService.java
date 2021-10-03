package com.example.realworld.application.tags.service;

import com.example.realworld.application.tags.dto.ResponseMultiTag;
import com.example.realworld.application.tags.persistence.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagBusinessService implements TagService {

    private final TagDomainService tagDomainService;

    /**
     * 전체 태그 정보 조회
     *
     * @return 전체 태그 정보 반환
     */
    @Transactional(readOnly = true)
    @Override
    public ResponseMultiTag getTags() {
        Set<Tag> tags = tagDomainService.findTagByAll();
        return ResponseMultiTag.from(tags);
    }
}
