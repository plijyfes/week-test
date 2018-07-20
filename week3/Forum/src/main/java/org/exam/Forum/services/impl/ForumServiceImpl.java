package org.exam.Forum.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.exam.Forum.dao.ArticleDao;
import org.exam.Forum.dao.LogDao;
import org.exam.Forum.dao.TagDao;
import org.exam.Forum.dao.UserDao;
import org.exam.Forum.entity.Article;
import org.exam.Forum.entity.Log;
import org.exam.Forum.entity.Tag;
import org.exam.Forum.entity.User;
import org.exam.Forum.services.ForumService;
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
	
	public User findOneUserByAccount(String account) {
		return userDao.findByAccount(account);
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
	
	public void saveArticle(Article article) {
		articleDao.save(article);
	}
	
	public void saveArticle(Article article, Article parent, User author) { //insert or update(to do:check User and child) 
		article.setParentArticle(parent);
		article.setUpdateTime(new Date(System.currentTimeMillis()));
		article.setAuthor(author);
		article.setVisible(true);
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
	
	public List<String> findAllTagsName() {
		List<Tag> list = tagDao.findAll();
		List<String> nameList = new ArrayList();
		for(Tag t:list) {
			nameList.add(t.getName());
		}
		return nameList;
	}
	
	public Tag findOneTagById(Integer id) {
		return tagDao.getOne(id);
	}
	
	public void saveTag(Tag tag) {
		tagDao.save(tag);
	}
	
	public List<Article> findAllVisible() {
		return articleDao.findByVisibleTrueOrderByUpdateTimeDesc();
	}
	
	public List<Article> findNew10MainArticle(Article parent) {
		return articleDao.findTop10ByVisibleTrueAndParentArticleOrderByUpdateTimeDesc(parent);
	}
	
	public List<Article> findNew10ChildArticle() {
		return articleDao.findTop10ByVisibleTrueAndParentArticleIsNotNullOrderByUpdateTimeDesc();
	}
	
	public List<Article> findNew10ArticleByUser(User user) {
		return articleDao.findTop10ByVisibleTrueAndAuthorOrderByUpdateTimeDesc(user);
	}

}
