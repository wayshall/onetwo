package org.onetwo.test.jorm.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.onetwo.common.hibernate.TimestampBaseEntity;
import org.onetwo.common.spring.dozer.DozerMapping;
import org.springframework.format.annotation.DateTimeFormat;


/*****
 * 用户表
 * @Entity
 */
@SuppressWarnings("serial")
@Entity
@Table(name="TEST_USER_AUTOID")
//@ASequenceGenerator(name="UserEntityGenerator", pkColumnValue="SEQ_ADMIN_USER")
//@DynamicUpdate
@DozerMapping
//@DataQueryFilter(fields={K.JOIN_FETCH, ".app.code"}, values={"apps:app", WebConstant.APP_CODE})
public class UserAutoidEntity {
	
	/*****
	 * 
	 */
	protected Long id;
  
	/*****
	 * 
	 */
	@Length(min=1, max=50)
	protected String userName;
  
	/*****
	 * 
	 */
	@Length(min=1, max=50)
	@NotBlank
	protected String nickName;

	  
	/*****
	 * 
	 */
	protected String password;
  
	/*****
	 * 
	 */
	@Length(min=0, max=50)
	@Email
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
	protected String appCode;
  
	public UserAutoidEntity(){
	}
	
	
	/*****
	 * 
	 * @return
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
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