package org.exam.Forum.services;

import org.exam.Forum.entity.Article;
import org.exam.Forum.entity.Log;
import org.exam.Forum.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ForumService {

	Log addLog(Log log);

	List<Log> getLogs();

	void deleteLog(Log log);
	
	List<User> findAllUsers();
	
	User findOneUserByAccount(String account);
	
	List<Article> findAllArticles();
	
	void saveArticle(Article article);
	
	User findOneUserById(Integer id);
	
	Article findOneArticleById(Integer id);
	
	void deleteArticle(Article article);

	List<Article> findNew10MainArticle(Article parent);
	
	List<Article> findNew10ChildArticle();
	
	List<Article> findNew10ArticleByUser(User user);
}
