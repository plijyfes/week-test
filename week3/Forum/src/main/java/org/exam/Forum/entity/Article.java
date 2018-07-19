package org.exam.Forum.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ARTICLE")
public class Article implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "SUBJECT")
	private String subject;

	@Column(name = "CONTENT")
	private String content;

	@Column(name = "UPDATE_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;

	@Column(name = "VISIBLE")
	private Boolean visible = true;

	@ManyToOne
	@JoinColumn(name = "PARENT_ARTICLE_ID", referencedColumnName = "ID")
	private Article parentArticle;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parentArticle", fetch=FetchType.EAGER)
	private List<Article> childArticle;

	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "articles")
	private List<Tag> tags;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "AUTHOR", referencedColumnName = "ID")
	private User author;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Article getParentArticle() {
		return parentArticle;
	}

	public void setParentArticle(Article parentArticle) {
		this.parentArticle = parentArticle;
	}

	public List<Article> getChildArticle() {
		return childArticle;
	}

	public void setChildArticle(List<Article> childArticle) {
		this.childArticle = childArticle;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}
}
