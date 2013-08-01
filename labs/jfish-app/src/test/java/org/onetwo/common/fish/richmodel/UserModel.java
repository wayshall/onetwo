package org.onetwo.common.fish.richmodel;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.richmodel.BaseModel;

@Entity
@Table(name="T_USER")
@SequenceGenerator(name="seqUser", sequenceName="SEQ_T_USER")
public class UserModel extends BaseModel  {

	static {
		System.out.println("UserModel init");
	}
	
	public void saveWith(String...relatedFields){
		super.saveWith(relatedFields);
	}
	private Long id;
	
	@NotBlank
	private String userName;

	@NotBlank
	private String password;

	private String status;
	
	@Email
	@NotBlank
	private String email;
	
	@JsonIgnore
	private Integer age;

	private Date birthDay;

	private Float height;

	private String statusString;
	
	private String readOnlyField;
	protected Date createTime;
	
	protected Date lastUpdateTime;
	

	private Collection<ArticleModel> articles;
	private Collection<RoleModel> roles = LangUtils.newArrayList();
	
//	@Override
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return id;
	}

	@Column(name="USER_NAME")
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

	@Column(name="status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name="password")
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

	@Column(name="CREATE_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name="LAST_UPDATE_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}


	@OneToMany(mappedBy="author")
	public Collection<ArticleModel> getArticles() {
		return articles;
	}

	public void setArticles(Collection<ArticleModel> articles) {
		this.articles = articles;
	}

	@ManyToMany
	@JoinTable(
			name="t_user_role",
			joinColumns=@JoinColumn(name="user_id"),
			inverseJoinColumns=@JoinColumn(name="role_id")
	)
	public Collection<RoleModel> getRoles() {
		return roles;
	}

	public void setRoles(Collection<RoleModel> roles) {
		this.roles = roles;
	}
	
	@Transient
	public Collection<RoleModel> getCascadeRoles(){
		return this.cascade("roles").list();
	}

}
