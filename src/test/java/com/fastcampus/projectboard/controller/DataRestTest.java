package com.fastcampus.projectboard.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("Data REST API 테스트")
@Transactional // 테스트 환경에서 롤백 상태로 transaction 실행
@AutoConfigureMockMvc
@SpringBootTest
public class DataRestTest {

    private final MockMvc mvc;

    public DataRestTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[api] 게시글 리스트 조회")
    @Test
    void givenNothing_wheRequestingArticles_thenReturnArticlesJsonResponse() throws Exception {
        // Given

        // When & THen
        mvc.perform(get("/api/articles"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.valueOf("application/hal+json")))
            .andDo(print());
    }

    @DisplayName("[api] 게시글 단건 조회")
    @Test
    void givenNothing_wheRequestingArticle_thenReturnArticleJsonResponse() throws Exception {
        // Given

        // When & THen
        mvc.perform(get("/api/articles/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.valueOf("application/hal+json")))
            .andDo(print());
    }

    @DisplayName("[api] 게시글 -> 댓글 리스트 조회")
    @Test
    void givenNothing_wheRequestingArticleCommentsFromArticle_thenReturnArticleCommentsJsonResponse()
        throws Exception {
        // Given

        // When & THen
        mvc.perform(get("/api/articles/1/articleComments"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.valueOf("application/hal+json")))
            .andDo(print());
    }

    @DisplayName("[api] 댓글 리스트 조회")
    @Test
    void givenNothing_wheRequestingArticleComments_thenReturnArticleCommentsJsonResponse()
        throws Exception {
        // Given

        // When & THen
        mvc.perform(get("/api/articleComments"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.valueOf("application/hal+json")))
            .andDo(print());
    }

    @DisplayName("[api] 댓글 단건 조회")
    @Test
    void givenNothing_wheRequestingArticleComment_thenReturnArticleCommentJsonResponse()
        throws Exception {
        // Given

        // When & THen
        mvc.perform(get("/api/articleComments/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.valueOf("application/hal+json")))
            .andDo(print());
    }
}
