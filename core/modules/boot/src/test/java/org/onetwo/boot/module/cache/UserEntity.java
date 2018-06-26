package org.onetwo.boot.module.cache;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;


/*****
 * 用户表
 * @Entity
 */
@Entity
@Table(name="TEST_USER")
//@TableGenerator(table=Constant.SEQ_TABLE_NAME, pkColumnName="GEN_NAME",valueColumnName="GEN_VALUE", pkColumnValue="SEQ_ADMIN_USER", allocationSize=50, initialValue=1, name="UserEntityGenerator")
@Data
public class UserEntity implements Serializable {
	
	/*****
	 * 
	 */
	@Id
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

	private Integer age;
	private Float height;

	
	//系统代码
	protected String appCode;
  

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