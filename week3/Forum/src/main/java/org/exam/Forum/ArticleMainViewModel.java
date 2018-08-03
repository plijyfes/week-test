package org.exam.Forum;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.exam.Forum.entity.Article;
import org.exam.Forum.entity.Tag;
import org.exam.Forum.entity.User;
import org.exam.Forum.services.AuthenticationService;
import org.exam.Forum.services.ForumService;
import org.exam.Forum.services.impl.ArticleTreeModel;
import org.exam.Forum.services.impl.ArticleTreeNode;
import org.exam.Forum.services.impl.InsertTask;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ArticleMainViewModel {

	final static Logger logger = Logger.getLogger(ArticleMainViewModel.class);

	@WireVariable
	private ForumService forumService;
	@WireVariable
	private AuthenticationService authenticationService;

	private ListModelList<Article> rootListModel;
	private ListModelList<Tag> tagListModel;
	private ListModelList<Tag> selectedtagListModel;
	private ArticleTreeModel treeModel;
	private ArticleTreeModel centerTreeModel;
	private Article parent;
	private Article formArticle;
	private Article singleArticleView;
	private User loginUser;
	// for post
	private boolean loading;
	private int waitTime;
	private int countdown;
	private ScheduledExecutorService executorService;
	private ScheduledFuture<?> future;
	private EventQueue<Event> que;

	@Init
	public void init() {
		// view
		refreshViewFromDB();
		Article root = forumService.findOneArticleById(1);
		parent = root;
		singleArticleView = new Article();
		formArticle = new Article();
		selectedtagListModel = new ListModelList<Tag>();
		// event
		que = EventQueues.lookup("update", EventQueues.APPLICATION, true);
		subscribe();
		executorService = Executors.newScheduledThreadPool(1);
		loginUser = forumService.findOneUserByAccount(authenticationService.getUserCredential().getAccount());
		loading = false;
		waitTime = 5;
		countdown = waitTime;
	}

	private void subscribe() {
		que.subscribe(new EventListener<Event>() {
			public void onEvent(Event evt) {
				refreshViewFromDB();
				refreshViewBinding();
			}
		});
	}

	public ListModelList<Article> getRootListModel() {
		return rootListModel;
	}

	@NotifyChange("rootListModel")
	public void setRootListModel(ListModelList<Article> rootListModel) {
		this.rootListModel = rootListModel;
	}

	public ListModelList<Tag> getTagListModel() {
		return tagListModel;
	}

	@NotifyChange("tagListModel")
	public void setTagListModel(ListModelList<Tag> tagListModel) {
		this.tagListModel = tagListModel;
	}

	public ListModelList<Tag> getSelectedtagListModel() {
		return selectedtagListModel;
	}

	@NotifyChange("selectedtagListModel")
	public void setSelectedtagListModel(ListModelList<Tag> selectedtagListModel) {
		this.selectedtagListModel = selectedtagListModel;
	}

	public ArticleTreeModel getTreeModel() {
		return treeModel;
	}

	@NotifyChange("treeModel")
	public void setTreeModel(ArticleTreeModel treeModel) {
		this.treeModel = treeModel;
	}

	public ArticleTreeModel getCenterTreeModel() {
		return centerTreeModel;
	}

	@NotifyChange("centerTreeModel")
	public void setCenterTreeModel(ArticleTreeModel centerTreeModel) {
		this.centerTreeModel = centerTreeModel;
	}

	public Article getParent() {
		return parent;
	}

	@NotifyChange("parent")
	public void setParent(Article parent) {
		this.parent = parent;
	}

	public Article getFormArticle() {
		return formArticle;
	}

	@NotifyChange("formArticle")
	public void setFormArticle(Article formArticle) {
		this.formArticle = formArticle;
	}

	public Article getSingleArticleView() {
		return singleArticleView;
	}

	@NotifyChange("singleArticleView")
	public void setSingleArticleView(Article singleArticleView) {
		this.singleArticleView = singleArticleView;
	}

	public User getLoginUser() {
		return loginUser;
	}

	@NotifyChange("loginUser")
	public void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
	}

	public boolean isLoading() {
		return loading;
	}

	@NotifyChange("loading")
	public void setLoading(boolean loading) {
		this.loading = loading;
	}

	public int getCountdown() {
		return countdown;
	}

	@NotifyChange("countdown")
	public void setCountdown(int countdown) {
		this.countdown = countdown;
	}

	public int getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}

	private ArticleTreeNode loadOnce(Article article) {
		ArticleTreeNode node = new ArticleTreeNode(article);
		node.setLoaded(true);
		for (Article sub : article.getChildArticle()) {
			if (sub.getVisible() != false) {
				ArticleTreeNode n = loadOnce(sub);
				node.add(n);
			}
		}
		return node;
	}

	private ArticleTreeNode listClickLoad(Article article, Article target) {
		ArticleTreeNode node = new ArticleTreeNode(article);
		node.setLoaded(true);
		for (Article sub : article.getChildArticle()) {
			if (sub.getVisible() != false && sub.getId() == target.getId()) {
				ArticleTreeNode n = loadOnce(sub);
				node.add(n);
			}
		}
		return node;
	}

	private void setChildrenVisible(Article deleteTarget) {
		List<Article> childrenList = deleteTarget.getChildArticle();
		if (childrenList != null && !childrenList.isEmpty()) {
			for (Article child : childrenList) {
				if (child.getVisible() != false) {
					child.setVisible(false);
					logger.warn("delete article: " + child.getSubject() + " author: " + child.getAuthor().getAccount());
					forumService.saveArticle(child);
					setChildrenVisible(child);
				}
			}
		}
	}

	@NotifyChange("centerTreeModel")
	@Command
	public void clickOnList(@BindingParam("target") ArticleTreeNode target) {
		Article newRoot = target.getData().getParentArticle();
		ArticleTreeNode newNode = listClickLoad(newRoot, target.getData());
		centerTreeModel = new ArticleTreeModel(newNode, true);
		// centerTreeModel = new ArticleTreeModel(loadOnce(target.getData()));
	}

	@NotifyChange("singleArticleView")
	@Command
	public void clickOnTree(@BindingParam("target") ArticleTreeNode target) {
		// System.out.println("tree click");
		singleArticleView = target.getData();
	}

	@NotifyChange({ "formArticle", "parent", "selectedtagListModel" })
	@Command
	public void newPost() {
		parent = forumService.findOneArticleById(1);
		selectedtagListModel = new ListModelList<Tag>();
		formArticle = new Article();
	}

	@NotifyChange({ "formArticle", "parent", "selectedtagListModel" })
	@Command
	public void goReply(@BindingParam("target") ArticleTreeNode target) {
		if (target != null) {
			parent = target.getData();
			selectedtagListModel = new ListModelList<Tag>();
			formArticle = new Article();
			// Executions.sendRedirect("/pages/post.zul");
		}
	}

	@NotifyChange({ "formArticle", "parent", "selectedtagListModel" })
	@Command
	public void edit(@BindingParam("target") ArticleTreeNode target) throws Exception {
		if (target.getData().getAuthor().getId() == loginUser.getId() && target.getData().getChildArticle().isEmpty()) {
			parent = target.getData().getParentArticle();
			formArticle = target.getData();
			ArrayList<Tag> subtaglist = new ArrayList<Tag>(formArticle.getTags());
			selectedtagListModel = new ListModelList<Tag>(subtaglist);
		} else {
			logger.warn("fail edit articleId:" + target.getData().getId() + " by:" + loginUser.getAccount());
			throw new Exception(Labels.getLabel("editalert"));
		}
	}

	// insert or update
	@NotifyChange({ "loading", "selectedtagListModel", "formArticle" })
	@Command
	public void save() throws InterruptedException, ExecutionException {
		formArticle.setParentArticle(parent);
		formArticle.setAuthor(loginUser);
		formArticle.setUpdateTime(new Date(System.currentTimeMillis()));
		formArticle.setVisible(true);
		ArrayList<Tag> tagarray = new ArrayList<Tag>();
		Object[] objarray = selectedtagListModel.toArray();
		for (Object obj : objarray) {
			tagarray.add((Tag) obj);
		}
		Set<Tag> mySet = new HashSet<Tag>(tagarray);
		formArticle.setTags(mySet);
		if (formArticle.getId() == null) {
			loading = true;
			InsertTask task = new InsertTask(formArticle, forumService, que, this);
			future = executorService.schedule(task, waitTime, TimeUnit.SECONDS);
		} else {
			forumService.saveArticle(formArticle);
			selectedtagListModel = new ListModelList<Tag>();
			formArticle = new Article();
		}
	}

	@NotifyChange("loading")
	@Command
	public void cancelSave() throws InterruptedException, ExecutionException {
		EventListener<ClickEvent> listener = new org.zkoss.zk.ui.event.EventListener<ClickEvent>() {
			public void onEvent(ClickEvent e) {
				if (e.getName().equals("onYes")) {
					future.cancel(true);
					loading = false;
//					refreshViewFromDB();
				}
			}
		};
		Messagebox.Button[] btn = { Messagebox.Button.YES, Messagebox.Button.NO };
		Messagebox.show(Labels.getLabel("cancelquestion"), "Question", btn, Messagebox.QUESTION,
				Messagebox.Button.CANCEL, listener);
	}

	@Command
	public void delete(@BindingParam("target") ArticleTreeNode target) throws Exception {
		Article targetArticle = target.getData();
		if (!loginUser.getAccount().equals(targetArticle.getAuthor().getAccount())) {
			logger.warn("fail delete articleId:" + targetArticle.getId() + " by:" + loginUser.getAccount());
			throw new Exception(Labels.getLabel("editalert"));
		}
		targetArticle.setVisible(false);
		logger.warn("delete article: " + targetArticle.getSubject() + " author: "
				+ targetArticle.getAuthor().getAccount() + " byUser: " + loginUser.getAccount());
		forumService.saveArticle(targetArticle);
		setChildrenVisible(targetArticle);
		que.publish(new Event("onUpdate", null));
	}

	public void refreshViewFromDB() {
		// logger.debug("refreshViewFromDB() start.");
		Article root = forumService.findOneArticleById(1);
		// singleArticleView = new Article();
		List<Article> mainList = forumService.findAllVisibleMain(root);
		rootListModel = new ListModelList<Article>(mainList);
		List<Tag> tagList = forumService.findAllTags();
		tagListModel = new ListModelList<Tag>(tagList);
		ArticleTreeNode rootNode = loadOnce(root);
		treeModel = new ArticleTreeModel(rootNode, true);
		centerTreeModel = new ArticleTreeModel(rootNode, true);
		// BindUtils.postNotifyChange(null, null, this, "singleArticleView");
	}
	
	public void refreshViewBinding() {
		BindUtils.postNotifyChange(null, null, this, "rootListModel");
		BindUtils.postNotifyChange(null, null, this, "tagListModel");
		BindUtils.postNotifyChange(null, null, this, "treeModel");
		BindUtils.postNotifyChange(null, null, this, "centerTreeModel");
		BindUtils.postNotifyChange(null, null, this, "loading");
	}

	@NotifyChange("countdown")
	@Command
	public void timer() {
		if (countdown > 0) {
			countdown -= 1;
		} else {
			countdown = waitTime;
		}
	}
}
