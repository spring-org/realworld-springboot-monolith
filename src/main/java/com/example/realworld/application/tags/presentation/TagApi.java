package com.example.realworld.application.tags.presentation;

import com.example.realworld.application.tags.dto.ResponseMultiTag;
import com.example.realworld.application.tags.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/tags")
@RequiredArgsConstructor
public class TagApi {

    private final TagService tagService;

    /**
     * 전체 태그 리스트를 조회
     *
     * @return 모든 태그 정보 반환
     */
    @GetMapping
    public ResponseEntity<ResponseMultiTag> getTags() {

        ResponseMultiTag responseMultiTag = tagService.getTags();

        return ResponseEntity.status(HttpStatus.OK).body(responseMultiTag);
    }
}
