package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.QArticle;
import com.fastcampus.projectboard.domain.projection.ArticleProjection;
import com.fastcampus.projectboard.repository.querydsl.ArticleRepositoryCustom;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * QuerydslPredicateExecutor : 해당 클래스에 대한 기본 검색 기능 추가 QuerydslBinderCustomizer : 조건에 맞는 검색 기능 추가
 */
@RepositoryRestResource(excerptProjection = ArticleProjection.class)
public interface ArticleRepository extends
	JpaRepository<Article, Long>,
	QuerydslPredicateExecutor<Article>,
	QuerydslBinderCustomizer<QArticle>,
	ArticleRepositoryCustom {

	Page<Article> findByTitleContaining(String title, Pageable pageable);

	Page<Article> findByContentContaining(String content, Pageable pageable);

	Page<Article> findByUserAccount_UserIdContaining(String userId, Pageable pageable);

	Page<Article> findByUserAccount_NicknameContaining(String nickname, Pageable pageable);

	void deleteByIdAndUserAccount_UserId(Long articleId, String userid);

	@Override
	default void customize(QuerydslBindings bindings, QArticle root) {
		bindings.excludeUnlistedProperties(true); // including에 포함 되지 않은 필드는 검색에서 제외
		// 원하는 필드 추가
		bindings.including(root.title, root.content, root.hashtags, root.createdAt, root.createdBy);
		// 필드 별 검색 조건
		// bindings.bind(root.title).first(StringExpression::likeIgnoreCase); // like '${v}'
		bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // like '%s{v}%'
		bindings.bind(root.hashtags.any().hashtagName).first(StringExpression::containsIgnoreCase);
		bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
		bindings.bind(root.createdAt).first(DateTimeExpression::eq);
		bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
	}
}
