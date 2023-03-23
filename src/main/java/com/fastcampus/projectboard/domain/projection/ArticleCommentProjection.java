package com.fastcampus.projectboard.domain.projection;

import java.time.LocalDateTime;

import org.springframework.data.rest.core.config.Projection;

import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.domain.UserAccount;

@Projection(name = "withUserAccount", types = ArticleComment.class)
public interface ArticleCommentProjection {
	Long getId();

	UserAccount getUserAccount();

	Long getParentCommentId();

	String getContent();

	LocalDateTime getCreatedAt();

	String getCreatedBy();

	LocalDateTime getModifiedAt();

	String getModifiedBy();
}
