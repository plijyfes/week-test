package org.exam.Forum;

import java.util.List;

import org.exam.Forum.entity.Article;
import org.exam.Forum.services.ForumService;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ArticleViewModel {

	@WireVariable
	private ForumService forumService;

	private ListModelList<Article> logListModel;
	private String subject;

	@Init
	public void init() {
		List<Article> articleList = forumService.findNew10Article(true);
		logListModel = new ListModelList<Article>(articleList);
	}

	public ListModel<Article> getLogListModel() {
		return logListModel;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Command
	public void addArticle() {
		if (Strings.isBlank(subject)) {
			return;
		}
		Article article = new Article();
		article.setSubject(subject);
		article.setContent("content");
//		article.setAuthor(forumService.findOneUserById(1)); lazy no session
		forumService.saveArticle(article);
		logListModel.add(article);
	}

	@Command
	public void deleteArticle(@BindingParam("article") Article article) {
		forumService.deleteArticle(article);
		logListModel.remove(article);
	}
}
