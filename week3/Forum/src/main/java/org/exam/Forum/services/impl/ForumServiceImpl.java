package org.exam.Forum.services.impl;

import java.util.Date;
import java.util.List;

import org.exam.Forum.entity.Article;
import org.exam.Forum.entity.Log;
import org.exam.Forum.entity.Tag;
import org.exam.Forum.entity.User;
import org.exam.Forum.services.ArticleDao;
import org.exam.Forum.services.ForumService;
import org.exam.Forum.services.TagDao;
import org.exam.Forum.services.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("forumService")
@Transactional
@Scope(value = "singleton", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ForumServiceImpl implements ForumService {

	@Autowired
	LogDao dao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	ArticleDao articleDao;
	
	@Autowired
	TagDao tagDao;

	public Log addLog(Log log) {
		return dao.save(log);
	}

	public List<Log> getLogs() {
		return dao.queryAll();
	}

	public void deleteLog(Log log) {
		dao.delete(log);
	}
// User CRUD
	public List<User> findAllUsers() {
		return userDao.findAll();
	}
	
	public User findOneUserById(Integer id) {
		return userDao.getOne(id);
	}
	
	public void saveUser(User user) {
		userDao.save(user);
	}
// Article CRUD
	public List<Article> findAllArticles() {
		return articleDao.findAll();
	}
	
	public Article findOneArticleById(Integer id) {
		return articleDao.getOne(id);
	}
	
	public void saveArticle(Article article) { //insert or update(to do:check User and child) 
		article.setUpdateTime(new Date(System.currentTimeMillis()));
		articleDao.save(article);
	}
	
	public void deleteArticle(Article article) {
		article.setVisible(false);
		articleDao.save(article);
	}
// Tag CRUD	
	public List<Tag> findAllTags() {
		return tagDao.findAll();
	}
	
	public Tag findOneTagById(Integer id) {
		return tagDao.getOne(id);
	}
	
	public void saveTag(Tag tag) {
		tagDao.save(tag);
	}

	@Override
	public List<Article> findNew10Article(Boolean visible) {
		return articleDao.findTop10ByVisibleOrderByUpdateTimeDesc(visible);
	}
	
	
}
