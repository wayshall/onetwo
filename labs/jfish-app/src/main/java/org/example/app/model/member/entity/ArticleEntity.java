package org.example.app.model.member.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.example.app.model.member.entity.UserEntity.PasswordOnly;
import org.example.app.model.utils.BaseEntity;
import org.hibernate.validator.constraints.NotBlank;
import org.onetwo.common.spring.validator.ValidationInfo;

@SuppressWarnings("serial")
@Entity
@Table(name="T_ARTICLE")
@SequenceGenerator(name="seqArticle", sequenceName="SEQ_T_ARTICLE")
public class ArticleEntity extends BaseEntity<Long>  {
	
	private Long id;
	
	@NotBlank(groups=PasswordOnly.class)
	@ValidationInfo(label="标题")
	private String title;
	private String content;
	
	private ColumnEntity column;
	
	private UserEntity author;

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="title")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name="content")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@ManyToOne
	@JoinColumn(name="user_id")
	public UserEntity getAuthor() {
		return author;
	}
	public void setAuthor(UserEntity author) {
		this.author = author;
	}
	
//	@ManyToOne
	@Transient
	public ColumnEntity getColumn() {
		return column;
	}
	public void setColumn(ColumnEntity column) {
		this.column = column;
	}

}
