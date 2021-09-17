package com.example.realworld.application.tags.repository;

import com.example.realworld.application.articles.domain.Article;
import com.example.realworld.application.articles.repository.ArticleRepository;
import com.example.realworld.application.tags.domain.Tag;
import com.example.realworld.application.users.domain.User;
import com.example.realworld.application.users.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TagRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private TagRepository tagRepository;

    @DisplayName("태그 생성 테스트")
    @Test
    void when_generateTag_expected_success_newTag() {
        Tag tag = Tag.of("Java");

        Tag savedTag = tagRepository.save(tag);

        assertThat(savedTag.getTagName()).isEqualTo(tag.getTagName());
    }

    @DisplayName("글에 태그 생성하는 테스트")
    @Test
    void when_attachTag_expected_success_attachTag() {
        // given
        User author = User.of("seokrae@gmail.com", "1234");
        User savedUser = userRepository.save(author);
        Article article = Article.of("title", "description", "body", savedUser);
        Article savedArticle = articleRepository.save(article);

        // when
        Tag tag = Tag.of("Java");
        Tag savedTag = tagRepository.save(tag);
        savedArticle.hashTag(savedTag);
        boolean containsTag = savedArticle.getTags().contains(tag);

        // then
        assertThat(containsTag).isTrue();
    }
}