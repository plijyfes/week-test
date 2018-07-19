package org.exam.Forum.services.impl;

import org.exam.Forum.entity.Article;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.TreeNode;

public class ArticleTreeModel extends DefaultTreeModel<Article> {

	public ArticleTreeModel(TreeNode<Article> root) {
		super(root);
	}
}
