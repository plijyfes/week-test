package org.exam.Forum;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.exam.Forum.entity.Article;
import org.exam.Forum.entity.Tag;
import org.exam.Forum.entity.User;
import org.exam.Forum.services.AuthenticationService;
import org.exam.Forum.services.ForumService;
import org.exam.Forum.services.impl.ArticleTreeModel;
import org.exam.Forum.services.impl.ArticleTreeNode;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModelList;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ArticleMainViewModel {

	final static Logger logger = Logger.getLogger(ArticleMainViewModel.class);

	@WireVariable
	private ForumService forumService;
	@WireVariable
	private AuthenticationService authenticationService;

	private ListModelList<Article> rootListModel;
	private ListModelList<Tag> tagListModel;
	private ArticleTreeModel treeModel;
	private ArticleTreeModel centerTreeModel;
	private Article parent;
	private Article formArticle;
	private Article singleArticleView;
	private User loginUser;

	@Init
	public void init() {
		Article root = forumService.findOneArticleById(1);
		parent = root;
		singleArticleView = new Article();
		formArticle = new Article();
		List<Article> mainList = forumService.findAllVisibleMain(root);
		rootListModel = new ListModelList<Article>(mainList);
		List<Tag> tagList = forumService.findAllTags();
		tagListModel = new ListModelList<Tag>(tagList);
		ArticleTreeNode rootNode = loadOnce(root);
		treeModel = new ArticleTreeModel(rootNode, true);
		centerTreeModel = new ArticleTreeModel(rootNode, true);
		loginUser = forumService.findOneUserByAccount(authenticationService.getUserCredential().getAccount());
		// System.out.println(forumService.findOneArticleById(21).getTags());
	}

	public ListModelList<Article> getAllListModel() {
		return rootListModel;
	}

	public ArticleTreeModel getTreeModel() {
		return treeModel;
	}

	public ArticleTreeModel getCenterTreeModel() {
		return centerTreeModel;
	}

	public void setCenterTreeModel(ArticleTreeModel centerTreeModel) {
		this.centerTreeModel = centerTreeModel;
	}

	public Article getParent() {
		return parent;
	}

	public void setParent(Article parent) {
		this.parent = parent;
	}

	public Article getFormArticle() {
		return formArticle;
	}

	public void setFormArticle(Article formArticle) {
		this.formArticle = formArticle;
	}

	public ListModelList<Tag> getTagListModel() {
		return tagListModel;
	}

	public void setTagListModel(ListModelList<Tag> tagListModel) {
		this.tagListModel = tagListModel;
	}

	public void setAllListModel(ListModelList<Article> allListModel) {
		this.rootListModel = allListModel;
	}

	public void setTreeModel(ArticleTreeModel treeModel) {
		this.treeModel = treeModel;
	}

	public Article getSingleArticleView() {
		return singleArticleView;
	}

	public void setSingleArticleView(Article singleArticleView) {
		this.singleArticleView = singleArticleView;
	}

	public User getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
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
			if (sub.getVisible() != false && sub.equals(target)) {
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

	@NotifyChange("formArticle")
	@Command
	public void newPost() {
		parent = forumService.findOneArticleById(1);
		formArticle = new Article();
	}

	@NotifyChange("formArticle")
	@Command
	public void goReply(@BindingParam("target") ArticleTreeNode target) {
		if (target != null) {
			parent = target.getData();
			formArticle = new Article();
			// Executions.sendRedirect("/pages/post.zul");
		}
	}

	@NotifyChange("formArticle")
	@Command
	public void edit(@BindingParam("target") ArticleTreeNode target) throws Exception {
		if (target.getData().getAuthor().equals(loginUser) && target.getData().getChildArticle().isEmpty()) {
			parent = target.getData().getParentArticle();
			formArticle = target.getData();
		} else {
			throw new Exception("you are not Author or the Article are not allowed to edit.");
		}
	}

	// insert or update
	@Command
	public void save() {
		User login = forumService.findOneUserByAccount(authenticationService.getUserCredential().getAccount());
		formArticle.setParentArticle(parent);
		formArticle.setAuthor(login);
		formArticle.setUpdateTime(new Date(System.currentTimeMillis()));
		formArticle.setVisible(true);
		forumService.saveArticle(formArticle);
		// forumService.saveArticle(formArticle, parent, login);
		Executions.sendRedirect("/index.zul");
	}

	@Command
	public void delete(@BindingParam("target") ArticleTreeNode target) throws Exception {
		Article targetArticle = target.getData();
		// String loginAccount = authenticationService.getUserCredential().getAccount();
		if (!loginUser.getAccount().equals(targetArticle.getAuthor().getAccount())) {
			throw new Exception("you are not Author.");
			// return;
		}
		targetArticle.setVisible(false);
		logger.warn("delete article: " + targetArticle.getSubject() + " author: "
				+ targetArticle.getAuthor().getAccount() + " byUser: " + loginUser.getAccount());
		forumService.saveArticle(targetArticle);
		setChildrenVisible(targetArticle);
		Executions.sendRedirect("/index.zul");
	}

}
