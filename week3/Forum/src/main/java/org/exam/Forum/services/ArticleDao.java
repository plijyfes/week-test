package org.exam.Forum.services;

import java.util.List;

import org.exam.Forum.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleDao extends JpaRepository<Article, Integer> {

	List<Article> findTop10ByVisibleOrderByUpdateTimeDesc(Boolean visible);
}
