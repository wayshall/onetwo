package org.onetwo.common.dbm.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.onetwo.common.db.CrudEntityManager;
import org.onetwo.dbm.support.Dbms;
import org.springframework.format.annotation.DateTimeFormat;


/*****
 * 用户表
 * @Entity
 */
@SuppressWarnings("serial")
//@SequenceGenerator(name="UserEntityGenerator", pkColumnValue="SEQ_ADMIN_USER")
//@DataQueryFilter(fields={K.JOIN_FETCH, ".app.code"}, values={"apps:app", WebConstant.APP_CODE})
@Entity
@Table(name="TEST_USER_AUTOID")
public class UserAutoidEntity implements Serializable {
	

	final static public CrudEntityManager<UserAutoidEntity, Long> crudManager = Dbms.newCrudManager(UserAutoidEntity.class);

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name="ID")
	protected Long id;
	@Length(min=1, max=50)
	protected String userName;
	@Length(min=0, max=50)
	@Email
	protected String email;
	protected String mobile;
	protected UserStatus status;
	
  
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
	protected Integer gender;
	
  
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
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public Date getBirthday() {
		return this.birthday;
	}
	
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

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
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appCode == null) ? 0 : appCode.hashCode());
		result = prime * result
				+ ((birthday == null) ? 0 : birthday.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
		result = prime * result
				+ ((nickName == null) ? 0 : nickName.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
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
		UserAutoidEntity other = (UserAutoidEntity) obj;
		if (appCode == null) {
			if (other.appCode != null)
				return false;
		} else if (!appCode.equals(other.appCode))
			return false;
		if (birthday == null) {
			if (other.birthday != null)
				return false;
		} else if (!birthday.equals(other.birthday))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (mobile == null) {
			if (other.mobile != null)
				return false;
		} else if (!mobile.equals(other.mobile))
			return false;
		if (nickName == null) {
			if (other.nickName != null)
				return false;
		} else if (!nickName.equals(other.nickName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (status != other.status)
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
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