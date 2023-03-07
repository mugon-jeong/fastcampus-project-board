package com.fastcampus.projectboard.dto.response;

import com.fastcampus.projectboard.dto.ArticleCommentDto;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public record ArticleCommentResponse(
    Long id,
    String content,
    LocalDateTime createdAt,
    String email,
    String nickname,
    String userId,
    Long parentCommentId,
    Set<ArticleCommentResponse> childComments
) {

    public static ArticleCommentResponse of(Long id, String content, LocalDateTime createdAt,
        String email, String nickname, String userId) {
        return new ArticleCommentResponse(id, content, createdAt, email, nickname, userId, null,
            null);
    }

    public static ArticleCommentResponse of(Long id, String content, LocalDateTime createdAt,
        String email, String nickname, String userId, Long parentCommentId) {
        Comparator<ArticleCommentResponse> childCommentComparator = Comparator
            .comparing(ArticleCommentResponse::createdAt) // createAt 기준으로 정렬
            .thenComparingLong(ArticleCommentResponse::id); // 같은 시간이면 id로 정렬
        return new ArticleCommentResponse(id, content, createdAt, email, nickname, userId,
            parentCommentId,
            new TreeSet<>(childCommentComparator));
    }

    public static ArticleCommentResponse from(ArticleCommentDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }

        return ArticleCommentResponse.of(
            dto.id(),
            dto.content(),
            dto.createdAt(),
            dto.userAccountDto().email(),
            nickname,
            dto.userAccountDto().userId(),
            dto.parentCommentId()
        );
    }

    public boolean hasParentComment() {
        return parentCommentId != null;
    }

}

