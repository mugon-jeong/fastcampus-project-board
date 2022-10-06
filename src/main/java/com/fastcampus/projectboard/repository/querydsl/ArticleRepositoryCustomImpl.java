package com.fastcampus.projectboard.repository.querydsl;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.QArticle;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

// 뒤에 Impl 은 약속
// 붙여줘야 querydsl이 인식함
public class ArticleRepositoryCustomImpl extends QuerydslRepositorySupport implements
    ArticleRepositoryCustom {

    public ArticleRepositoryCustomImpl() {
        super(Article.class);
    }

    @Override
    public List<String> findAllDistinctHashtags() {
        QArticle article = QArticle.article;

//        JPQLQuery<String> query = from(article)
//            .distinct()
//            .select(article.hashtag)
//            .where(article.hashtag.isNotNull());
//        return query.fetch();

        return from(article)
            .distinct()
            .select(article.hashtag)
            .where(article.hashtag.isNotNull())
            .fetch();
    }
}
