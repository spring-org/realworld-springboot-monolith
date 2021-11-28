package com.example.realworld.application.tags.service;

import com.example.realworld.application.tags.dto.ResponseMultiTag;
import com.example.realworld.application.tags.persistence.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TagBusinessServiceTest {

    @InjectMocks
    private TagBusinessService tagBusinessService;

    @Mock
    private TagDomainService tagDomainService;

    @DisplayName("태그 조회 테스트")
    @Test
    void testCase1() {
        Set<Tag> tags = Set.of(Tag.of("Java"), Tag.of("JS"), Tag.of("Python"));
        given(tagDomainService.findTagByAll()).willReturn(tags);

        ResponseMultiTag multiTag = tagBusinessService.getTags();

        then(tagDomainService).should(times(1)).findTagByAll();
        assertThat(multiTag.getTagList()).contains("Java", "JS", "Python");
    }
}