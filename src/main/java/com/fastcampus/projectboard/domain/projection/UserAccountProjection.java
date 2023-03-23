package com.fastcampus.projectboard.domain.projection;

import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;

import com.fastcampus.projectboard.domain.UserAccount;

@Projection(name = "withoutPassword", types = UserAccount.class)
public interface UserAccountProjection {
	String getUserId();

	String getEmail();

	String getNickname();

	String getMemo();

	LocalDateTime getCreatedAt();

	String getCreatedBy();

	LocalDateTime getModifiedAt();

	String getModifiedBy();
}
