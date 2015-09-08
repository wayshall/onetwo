package org.onetwo.common.utils.list;

import java.util.Date;


/*****
 * @Entity
 */
@SuppressWarnings("serial")
public class UserEntity {
	
	/*****
	 * 
	 */
	protected Long id;
  
	/*****
	 * 
	 */
	protected String userName;
  
	protected int age;
	/*****
	 * 
	 */
	protected String nickName;

	  
	/*****
	 * 
	 */
	protected String password;
  
	/*****
	 * 
	 */
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
  
	public UserEntity(){
	}
	
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
	

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
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