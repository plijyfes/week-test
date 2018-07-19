package org.exam.Forum;

import java.util.List;

import org.exam.Forum.entity.Article;
import org.exam.Forum.services.AuthenticationService;
import org.exam.Forum.services.ForumService;
import org.exam.Forum.services.impl.ArticleTreeModel;
import org.exam.Forum.services.impl.ArticleTreeNode;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModelList;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ArticleMainViewModel {

	@WireVariable
	private ForumService forumService;
	@WireVariable
	private AuthenticationService authenticationService;

	private ListModelList<Article> allListModel;
	private ArticleTreeModel treeModel;

	@Init
	public void init() {
		Article root = forumService.findOneArticleById(1);
		List<Article> allList = forumService.findAllVisible();
		allListModel = new ListModelList<Article>(allList);
		ArticleTreeNode rootNode = loadOnce(root);
		treeModel = new ArticleTreeModel(rootNode);
	}

	public ListModelList<Article> getAllListModel() {
		return allListModel;
	}

	public ArticleTreeModel getTreeModel() {
		return treeModel;
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
}
