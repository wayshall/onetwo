package org.onetwo.common.fish.richmodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.example.app.model.member.entity.UserEntity.PasswordOnly;
import org.hibernate.validator.constraints.NotBlank;
import org.onetwo.common.spring.validator.ValidationInfo;
import org.onetwo.plugins.richmodel.BaseModel;

@SuppressWarnings("serial")
@Entity
@Table(name="T_ARTICLE")
@SequenceGenerator(name="seqArticle", sequenceName="SEQ_T_ARTICLE")
public class ArticleModel extends BaseModel  {
	
	private Long id;
	
	@NotBlank(groups=PasswordOnly.class)
	@ValidationInfo(label="标题")
	private String title;
	private String content;
	
	
	private UserModel author;

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
	public UserModel getAuthor() {
		return author;
	}
	public void setAuthor(UserModel author) {
		this.author = author;
	}

}
