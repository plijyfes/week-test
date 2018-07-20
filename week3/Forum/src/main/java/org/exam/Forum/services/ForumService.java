package org.exam.Forum.services;

import org.exam.Forum.entity.Article;
import org.exam.Forum.entity.Log;
import org.exam.Forum.entity.Tag;
import org.exam.Forum.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

public interface ForumService {

	Log addLog(Log log);

	List<Log> getLogs();

	void deleteLog(Log log);

	List<User> findAllUsers();

	User findOneUserByAccount(String account);
	
	public List<Tag> findAllTags();
	
	public List<String> findAllTagsName();

	List<Article> findAllArticles();

	void saveArticle(Article article, Article parent, User author, Set<Tag> tags);

	User findOneUserById(Integer id);

	Article findOneArticleById(Integer id);

	void deleteArticle(Article article);

	List<Article> findNew10MainArticle(Article parent);

	List<Article> findNew10ChildArticle();

	List<Article> findNew10ArticleByUser(User user);

	List<Article> findAllVisible();
}
