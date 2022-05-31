package com.example.realworld.application.articles.service;

import com.example.realworld.application.articles.exception.NotFoundArticleException;
import com.example.realworld.application.articles.persistence.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ArticleDomainServiceTest {

	@InjectMocks
	private ArticleDomainService articleDomainService;
	@Mock
	private ArticleRepository articleRepository;


	@DisplayName("글 조회 실패 예외 테스트")
	@Test
	void when_getArticle_expect_fail_exception() {
		given(articleRepository.findBySlugOrderByIdDesc(anyString())).willThrow(NotFoundArticleException.class);

		assertThatExceptionOfType(NotFoundArticleException.class)
				.isThrownBy(() -> articleDomainService.getArticleBySlug("no_data_slug"));
		;
	}
}