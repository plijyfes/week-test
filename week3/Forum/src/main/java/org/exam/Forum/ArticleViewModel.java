package org.exam.Forum;

import java.util.List;

import org.apache.log4j.Logger;
import org.exam.Forum.entity.Article;
import org.exam.Forum.services.AuthenticationService;
import org.exam.Forum.services.ForumService;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ArticleViewModel {
	final static Logger logger = Logger.getLogger(ArticleMainViewModel.class);

	@WireVariable
	private ForumService forumService;
	@WireVariable
	private AuthenticationService authenticationService;

	private ListModelList<Article> westListModel;
	private ListModelList<Article> centerListModel;
	private ListModelList<Article> eastListModel;
	private EventQueue<Event> que;

	@Init
	public void init() {
		que = EventQueues.lookup("update", EventQueues.APPLICATION, true);
		subscribe();
		Article root = forumService.findOneArticleById(1);
		List<Article> westList = forumService.findNew10MainArticle(root);
		List<Article> centerList = forumService.findNew10ChildArticle(root);
		String account = authenticationService.getUserCredential().getAccount();
		List<Article> eastList = forumService.findNew10ArticleByUser(forumService.findOneUserByAccount(account));
		westListModel = new ListModelList<Article>(westList);
		centerListModel = new ListModelList<Article>(centerList);
		eastListModel = new ListModelList<Article>(eastList);
	}

	public void subscribe() {
		que.subscribe(new EventListener<Event>() {
			public void onEvent(Event evt) {
				refreshViewFromDB();
			}
		});
	}

	public void refreshViewFromDB() {
//		logger.debug("refreshViewFromDB() at threelistVM start.");
		Article root = forumService.findOneArticleById(1);
		List<Article> westList = forumService.findNew10MainArticle(root);
		List<Article> centerList = forumService.findNew10ChildArticle(root);
		String account = authenticationService.getUserCredential().getAccount();
		List<Article> eastList = forumService
				.findNew10ArticleByUser(forumService.findOneUserByAccount(account));
		westListModel = new ListModelList<Article>(westList);
		centerListModel = new ListModelList<Article>(centerList);
		eastListModel = new ListModelList<Article>(eastList);
		BindUtils.postNotifyChange(null, null, this, "westListModel");
		BindUtils.postNotifyChange(null, null, this, "centerListModel");
		BindUtils.postNotifyChange(null, null, this, "eastListModel");
	}

	public ListModel<Article> getWestListModel() {
		return westListModel;
	}

	public ListModel<Article> getCenterListModel() {
		return centerListModel;
	}

	public ListModel<Article> getEastListModel() {
		return eastListModel;
	}

	@NotifyChange("westListModel")
	public void setWestListModel(ListModelList<Article> westListModel) {
		this.westListModel = westListModel;
	}

	@NotifyChange("centerListModel")
	public void setCenterListModel(ListModelList<Article> centerListModel) {
		this.centerListModel = centerListModel;
	}

	@NotifyChange("eastListModel")
	public void setEastListModel(ListModelList<Article> eastListModel) {
		this.eastListModel = eastListModel;
	}

}
