package com.fastcampus.projectboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.domain.Hashtag;
import com.fastcampus.projectboard.domain.UserAccount;

@Configuration
public class DataRestConfig {
	@Bean
	public RepositoryRestConfigurer repositoryRestConfigurer() {
		return RepositoryRestConfigurer.withConfig((config, cors) ->
			config
				.exposeIdsFor(UserAccount.class)
				.exposeIdsFor(Article.class)
				.exposeIdsFor(ArticleComment.class)
				.exposeIdsFor(Hashtag.class)
		);
	}
}
