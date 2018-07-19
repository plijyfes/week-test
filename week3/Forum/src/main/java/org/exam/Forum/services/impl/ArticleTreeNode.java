package org.exam.Forum.services.impl;

import java.util.LinkedList;

import org.exam.Forum.entity.Article;
import org.zkoss.zul.DefaultTreeNode;

public class ArticleTreeNode extends DefaultTreeNode<Article> {
	private static final long serialVersionUID = 1L;

	// flag for loaded
	boolean loaded;

	public ArticleTreeNode(Article article) {
		super(article, new LinkedList<ArticleTreeNode>());
	}

	// equals getData,
	public Article getArticle() {
		return getData();
	}

	// NOTE, set unit that has different hashcode and equals will cause
	// identification problem.
	public void setArticle(Article article) {
		setData(article);
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getData() == null) ? 0 : getData().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArticleTreeNode other = (ArticleTreeNode) obj;
		if (getData() == null) {
			if (other.getData() != null)
				return false;
		} else if (!getData().equals(other.getData()))
			return false;
		return true;
	}
}
