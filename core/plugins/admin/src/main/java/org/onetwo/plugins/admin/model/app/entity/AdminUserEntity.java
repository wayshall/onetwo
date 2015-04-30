package org.onetwo.plugins.admin.model.app.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.onetwo.common.db.ILogicDeleteEntity;
import org.onetwo.common.hibernate.TimestampBaseEntity;
import org.onetwo.common.spring.dozer.DozerMapping;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.admin.utils.WebConstant;
import org.onetwo.plugins.admin.utils.WebConstant.ValidGroup;
import org.onetwo.plugins.admin.utils.WebConstant.ValidGroup.ValidWhenEdit;
import org.onetwo.plugins.admin.utils.WebConstant.ValidGroup.ValidWhenNew;
import org.springframework.format.annotation.DateTimeFormat;


/*****
 * 用户表
 * @Entity
 */
@SuppressWarnings("serial")
@Entity
@Table(name="ADMIN_USER")
//@ASequenceGenerator(name="UserEntityGenerator", pkColumnValue="SEQ_ADMIN_USER")
@TableGenerator(table=WebConstant.SEQ_TABLE_NAME, pkColumnName="GEN_NAME",valueColumnName="GEN_VALUE", pkColumnValue="SEQ_ADMIN_USER", allocationSize=50, name="UserEntityGenerator")
//@SelectBeforeUpdate
//@DynamicUpdate
@DozerMapping
//@DataQueryFilter(fields={K.JOIN_FETCH, ".app.code"}, values={"apps:app", WebConstant.APP_CODE})
public class AdminUserEntity extends TimestampBaseEntity implements ILogicDeleteEntity {
	
	/*****
	 * 
	 */
	protected Long id;
  
	/*****
	 * 
	 */
	@Length(min=1, max=50, groups={Default.class, ValidWhenNew.class, ValidWhenEdit.class})
	protected String userName;
  
	/*****
	 * 
	 */
	@Length(min=1, max=50, groups={Default.class, ValidWhenNew.class, ValidWhenEdit.class})
	@NotBlank
	protected String nickName;

	  
	/*****
	 * 
	 */
	protected String password;
  
	/*****
	 * 
	 */
	@Length(min=0, max=50, groups={Default.class, ValidWhenNew.class, ValidWhenEdit.class})
	@Email(groups={Default.class, ValidWhenNew.class, ValidWhenEdit.class})
	protected String email;
  
	/*****
	 * 
	 */
	protected String mobile;
  
	/*****
	 * 
	 */
	protected Integer gender;
	protected UserStatus status;
	
	/*****
	 * 
	 */
	protected Date birthday;
	
	//系统代码
	@Length(min=0, max=50, groups={Default.class})
	protected String appCode;

//	@Length(min=6, max=50, groups={ValidWhenNew.class})
	@Pattern(regexp="[\\w\\p{Punct}]{6,50}", groups={ValidGroup.Password.class}, message="6到50个字符，空格除外")
	protected String confirmPassword;
	
	private List<AdminRoleEntity> roles;
	
	
  
	public AdminUserEntity(){
	}
	
	
	/*****
	 * 
	 * @return
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="UserEntityGenerator") 
	@Column(name="ID")
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="USER_NAME")
	public String getUserName() {
		return this.userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="NICK_NAME")
	public String getNickName() {
		return this.nickName;
	}
	
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="EMAIL")
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="MOBILE")
	public String getMobile() {
		return this.mobile;
	}
	
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="GENDER")
	public Integer getGender() {
		return this.gender;
	}
	
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="BIRTHDAY")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public Date getBirthday() {
		return this.birthday;
	}
	
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Column(name="PASSWORD")
	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	/*@Column(name="APP_CODE")
	public String getAppCode() {
		return appCode;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}*/

	

	@Enumerated(EnumType.STRING)
	public UserStatus getStatus() {
		return status;
	}


	public String getAppCode() {
		return appCode;
	}


	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}


	public void setStatus(UserStatus status) {
		this.status = status;
	}

	@Transient
	public String getConfirmPassword() {
		return confirmPassword;
	}


	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@ManyToMany
	@JoinTable(name="ADMIN_USER_ROLE", joinColumns=@JoinColumn(name="USER_ID"), inverseJoinColumns=@JoinColumn(name="ROLE_ID"))
	public List<AdminRoleEntity> getRoles() {
		return roles;
	}
	
	public void addRole(AdminRoleEntity role){
		if(roles==null)
			roles = LangUtils.newArrayList();
		roles.add(role);
	}


	public void setRoles(List<AdminRoleEntity> roles) {
		this.roles = roles;
	}

	@Override
	public void deleted() {
		setStatus(UserStatus.DELETE);
		getRoles().clear();
	}
	/****
	 * 是否需要绑定商户的角色
	 * @return
	
	@Transient
	public boolean isMerchantBindedRole(){
		if(LangUtils.isEmpty(getRoles()))
			return false;
		for(AdminRoleEntity role : getRoles()){
			if(role.isMerchantBindedRole())
				return true;
		}
		return false;
	}
	 */
	@Transient
	public boolean isBindTheRole(AdminRoleEntity role){
		for(AdminRoleEntity ur : getRoles()){
			if(ur.getId().equals(role.getId()))
				return true;
		}
		return false;
	}
	@Transient
	public boolean isSystemRoot(){
		if(LangUtils.isEmpty(getRoles()))
			return false;
		for(AdminRoleEntity role : getRoles()){
			if(role.isSystemRoot())
				return true;
		}
		return false;
	}

	public static enum UserStatus {
		NORMAL("正常"),
		STOP("停用"),
		DELETE("删除");
		
		private final String label;
		UserStatus(String label){
			this.label = label;
		}
		public String getLabel() {
			return label;
		}
		public String getValue(){
			return toString();
		}

	}
	public static enum UserGender {
		MALE("男"),
		FEMALE("女");
		
		private final String label;
		UserGender(String label){
			this.label = label;
		}
		public String getLabel() {
			return label;
		}
		public int getValue(){
			return ordinal();
		}

	}
}