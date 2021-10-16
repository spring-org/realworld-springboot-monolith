package com.example.realworld.application.articles.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MultiArticleApiTest {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String REPLACEMENT_EMPTY_DELIMITER = "";
    private String token;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

//    @DisplayName("글 페이징 조건 조회")
//    @Test
//    void when_getArticles_expect_success_page_article() throws Exception {
//        // user given
//        String email = "seokrae@gmail.com";
//        RequestSaveUser requestSaveUser = getRequestSaveUser(email);
//        String otherUserEmail = "other@gmail.com";
//        RequestSaveUser requestSaveOtherUser = getRequestSaveUser(otherUserEmail, "other");
//        // article given
//        RequestSaveArticle requestSaveArticle1 = getRequestSaveArticle(1, "Java");
//        RequestSaveArticle requestSaveArticle2 = getRequestSaveArticle(2, "JavaScript");
//        RequestSaveArticle requestSaveArticle3 = getRequestSaveArticle(3, "JavaScript");
//        RequestSaveArticle requestSaveArticle4 = getRequestSaveArticle(4, "Python");
//        RequestSaveArticle requestSaveArticle5 = getRequestSaveArticle(5, "JavaScript");
//        RequestArticleCondition pageCondition =
//                RequestArticleCondition.of("JavaScript", "other@gmail.com", null);
//        // when
//
//        // then
//        mockMvc.perform(
//                        get("/api/articles")
//                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//                                .content(mapper.writeValueAsString(pageCondition))
//                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.articles.[0].slug").value(responseSingleArticle5.getSlug()))
//                .andExpect(jsonPath("$.articles.[0].title").value(responseSingleArticle5.getTitle()))
//                .andExpect(jsonPath("$.articles.[0].description").value(responseSingleArticle5.getDescription()))
//                .andExpect(jsonPath("$.articles.[0].body").value(responseSingleArticle5.getBody()))
//                .andExpect(jsonPath("$.articles.[0].tagList.[0]").value("JavaScript"))
//                .andExpect(jsonPath("$.articles.[0].createdAt").exists())
//                .andExpect(jsonPath("$.articles.[0].updatedAt").exists())
//                .andExpect(jsonPath("$.articles.[0].favorited").value(false))
//                .andExpect(jsonPath("$.articles.[0].favoritesCount").value(0))
//                .andExpect(jsonPath("$.articles.[0].author.email").value("other@gmail.com"))
//                .andExpect(jsonPath("$.articles.[0].author.userName").value("other"))
//                .andExpect(jsonPath("$.articles.[0].author.following").value(false))
//                .andExpect(jsonPath("$.articles.[1].slug").value(responseSingleArticle3.getSlug()))
//                .andExpect(jsonPath("$.articles.[1].title").value(responseSingleArticle3.getTitle()))
//                .andExpect(jsonPath("$.articles.[1].description").value(responseSingleArticle3.getDescription()))
//                .andExpect(jsonPath("$.articles.[1].body").value(responseSingleArticle3.getBody()))
//                .andExpect(jsonPath("$.articleCount").value(2));
//    }
//
//    @DisplayName("피드 조회 테스트")
//    @Test
//    void when_getPeed_expect_success_following_article() throws Exception {
//        // user given
//        String email = "seokrae@gmail.com";
//        RequestSaveUser requestSaveUser = getRequestSaveUser(email);
//        String otherUserEmail = "other@gmail.com";
//        RequestSaveUser requestSaveOtherUser = getRequestSaveUser(otherUserEmail, "other");
//        String anotherUserEmail = "another@gmail.com";
//        RequestSaveUser requestSaveAnotherUser = getRequestSaveUser(anotherUserEmail, "another");
//        // article given
//        RequestSaveArticle requestSaveArticle1 = getRequestSaveArticle(1, "Java");
//        RequestSaveArticle requestSaveArticle2 = getRequestSaveArticle(2, "Java");
//        RequestSaveArticle requestSaveArticle3 = getRequestSaveArticle(3, "Python");
//        RequestSaveArticle requestSaveArticle4 = getRequestSaveArticle(4, "R");
//        RequestSaveArticle requestSaveArticle5 = getRequestSaveArticle(5, "JavaScript");
//
//        PageRequest pageRequest = PageRequest.of(0, 20);
//
//        // when
//        userService.postUser(requestSaveUser);
//        userService.postUser(requestSaveOtherUser);
//        userService.postUser(requestSaveAnotherUser);
//
//        String slug1 = articleService.postArticle(otherUserEmail, requestSaveArticle1).getSlug();
//        String slug2 = articleService.postArticle(otherUserEmail, requestSaveArticle2).getSlug();
//        String slug3 = articleService.postArticle(anotherUserEmail, requestSaveArticle3).getSlug();
//        String slug4 = articleService.postArticle(anotherUserEmail, requestSaveArticle4).getSlug();
//        String slug5 = articleService.postArticle(anotherUserEmail, requestSaveArticle5).getSlug();
//
//        favoriteArticleService.favoriteArticle(email, slug1);
//        favoriteArticleService.favoriteArticle(email, slug2);
//        favoriteArticleService.favoriteArticle(email, slug3);
//        favoriteArticleService.favoriteArticle(email, slug4);
//        favoriteArticleService.favoriteArticle(email, slug5);
//
//        // then
//        mockMvc.perform(
//                        get("/api/articles/feed")
//                                .session(session)
//                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//                                .content(mapper.writeValueAsString(pageRequest))
//                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
//                )
//                .andDo(print())
//                .andExpect(status().isOk());
//    }

}
