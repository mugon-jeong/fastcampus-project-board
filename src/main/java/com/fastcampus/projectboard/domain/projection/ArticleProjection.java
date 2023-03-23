package com.fastcampus.projectboard.domain.projection;

import java.time.LocalDateTime;

import org.springframework.data.rest.core.config.Projection;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.UserAccount;

@Projection(name = "withUserAccount", types = Article.class)
public interface ArticleProjection {
	Long getId();

	UserAccount getUserAccount();

	String getTitle();

	String getContent();

	LocalDateTime getCreatedAt();

	String getCreatedBy();

	LocalDateTime getModifiedAt();

	String getModifiedBy();
}
