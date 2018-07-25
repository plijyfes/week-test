package org.exam.Forum.services.impl;

import org.exam.Forum.entity.Article;
import org.exam.Forum.services.ForumService;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueue;


public class InsertTask implements Runnable {

	private ForumService forumService;

	private Article article;
	
	private EventQueue<Event> que;

	public InsertTask(Article article, ForumService forumService, EventQueue<Event> que) {
		super();
		this.forumService = forumService;
		this.article = article;
		this.que = que;
	}

	@Override
	public void run() {
		forumService.saveArticle(article);
		que.publish(new Event("onUpdate", null));
		System.out.println("save");
	}

}
