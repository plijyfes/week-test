package org.exam.Forum;

import java.util.List;

import org.exam.Forum.entity.Article;
import org.exam.Forum.services.AuthenticationService;
import org.exam.Forum.services.ForumService;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.TreeNode;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ArticleViewModel {

	@WireVariable
	private ForumService forumService;
	@WireVariable
	private AuthenticationService authenticationService;

	private ListModelList<Article> westListModel;
	private ListModelList<Article> centerListModel;
	private ListModelList<Article> eastListModel;
	private ArticleTreeModel treeModel;
	private String subject;

	@Init
	public void init() {
		Article root = forumService.findOneArticleById(1);
		List<Article> westList = forumService.findNew10MainArticle(root);
		List<Article> centerList = forumService.findNew10ChildArticle();
		String account = authenticationService.getUserCredential().getAccount();
		List<Article> eastList = forumService.findNew10ArticleByUser(forumService.findOneUserByAccount(account));
		westListModel = new ListModelList<Article>(westList);
		centerListModel = new ListModelList<Article>(centerList);
		eastListModel = new ListModelList<Article>(eastList);
		DefaultTreeNode<Article> rootNode = new DefaultTreeNode<Article>(root);
		treeModel = new ArticleTreeModel(rootNode);
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

	public ArticleTreeModel getTreeModel() {
		return treeModel;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
}
