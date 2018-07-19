package org.exam.Forum.dao;

import java.util.List;

import org.exam.Forum.entity.Article;
import org.exam.Forum.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleDao extends JpaRepository<Article, Integer> {
	
	List<Article> findByVisibleTrueOrderByUpdateTimeDesc();

	List<Article> findTop10ByVisibleTrueAndParentArticleOrderByUpdateTimeDesc(Article parent);
	
	List<Article> findTop10ByVisibleTrueAndParentArticleIsNotNullOrderByUpdateTimeDesc();
	
	List<Article> findTop10ByVisibleTrueAndAuthorOrderByUpdateTimeDesc(User user);
}
