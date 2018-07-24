package org.exam.Forum.services.impl;

import org.exam.Forum.entity.Article;
import org.exam.Forum.services.ForumService;


public class InsertTask implements Runnable {

	private ForumService forumService;

	private Article article;

	public InsertTask(Article article, ForumService forumService) {
		super();
		this.forumService = forumService;
		this.article = article;
	}

	@Override
	public void run() {
		forumService.saveArticle(article);
		System.out.println("save");
	}

}
