package org.exam.Forum.services.impl;

import org.exam.Forum.ArticleMainViewModel;
import org.exam.Forum.entity.Article;
import org.exam.Forum.entity.Tag;
import org.exam.Forum.services.ForumService;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zul.ListModelList;

public class InsertTask implements Runnable {

	private ForumService forumService;

	private Article article;

	private EventQueue<Event> que;

	private ArticleMainViewModel amvm;

	public InsertTask(Article article, ForumService forumService, EventQueue<Event> que, ArticleMainViewModel amvm) {
		super();
		this.forumService = forumService;
		this.article = article;
		this.que = que;
		this.amvm = amvm;
	}

	@Override
	public void run() {
		forumService.saveArticle(article);
		que.publish(new Event("onUpdate", null));
		amvm.setLoading(false);
		amvm.setSelectedtagListModel(new ListModelList<Tag>());
		amvm.setFormArticle(new Article());
		amvm.setCountdown(amvm.getWaitTime());
		// System.out.println("save");
	}

}
