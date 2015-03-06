package org.example.app.model.member.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.example.app.model.utils.BaseEntity;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
@Table(name="T_USER")
@org.nutz.dao.entity.annotation.Table("T_USER")
@SequenceGenerator(name="seqUser", sequenceName="SEQ_T_USER")
@XmlRootElement
//@JFishEntityListeners(TestEntityListener.class)
//@JFishFieldListeners(TestEntityListener.class)
public class UserEntity extends BaseEntity<Long>  {

	public static interface PasswordOnly {
	}
	
	@org.nutz.dao.entity.annotation.Id
	@org.nutz.dao.entity.annotation.Column("id")
	private Long id;

	@org.nutz.dao.entity.annotation.Column("USER_NAME")
	@NotBlank
	private String userName; 
  
	@NotBlank
	@org.nutz.dao.entity.annotation.Column("PASSWORD")
	private String password;

	private String status;
	
	@Email
	@org.nutz.dao.entity.annotation.Column("email")
	private String email;
	
	@org.nutz.dao.entity.annotation.Column("age")
	@JsonIgnore
	private Integer age;

	@org.nutz.dao.entity.annotation.Column("BIRTH_DAY")
	private Date birthDay;

	@org.nutz.dao.entity.annotation.Column("height")
	private Float height;

	private String statusString;
	
	private String readOnlyField;
	
	@NotNull(groups=PasswordOnly.class)
	@Valid
	private List<ArticleEntity> articles;
	
	private List<AddressEntity> address;
	
	
//	@Override
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return id;
	}

	@NotBlank(groups=PasswordOnly.class)
	@Size(max=10)
	@Column(name="USER_NAME")
//	@JFishFieldListeners(TestEntityListener.class)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name="EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name="AGE")
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Column(name="BIRTH_DAY")
	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	@Column(name="HEIGHT")
	public Float getHeight() {
		return height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name="PASSWORD")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Transient
	public String getStatusString() {
		return statusString;
	}

	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}

	@Column(name="READ_ONLY_FIELD", updatable=false, insertable=false)
	public String getReadOnlyField() {
		return readOnlyField;
	}

	public void setReadOnlyField(String readOnlyField) {
		this.readOnlyField = readOnlyField;
	}

	@OneToMany(mappedBy="author")
	public List<ArticleEntity> getArticles() {
		return articles;
	}

	public void setArticles(List<ArticleEntity> articles) {
		this.articles = articles;
	}

	@OneToMany
	@JoinColumn(name="USER_ID")
	public List<AddressEntity> getAddress() {
		return address;
	}

	public void setAddress(List<AddressEntity> address) {
		this.address = address;
	}

	
}
