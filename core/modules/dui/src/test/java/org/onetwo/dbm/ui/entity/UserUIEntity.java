
package org.onetwo.dbm.ui.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.onetwo.dbm.annotation.SnowflakeId;
import org.onetwo.dbm.jpa.BaseEntity;
import org.onetwo.dbm.mapping.DbmEnumIntMapping;
import org.onetwo.dbm.ui.annotation.DUIEntity;
import org.onetwo.dbm.ui.annotation.DUIField;
import org.onetwo.dbm.ui.annotation.DUISelect;

import lombok.Data;
import lombok.EqualsAndHashCode;

/***
 * 租户表
 */
@SuppressWarnings("serial")
@Entity
@Table(name="TEST_USER")
@Data
@EqualsAndHashCode(callSuper=true)
@DUIEntity(name = "TestUser", label = "用户")
public class UserUIEntity extends BaseEntity {

    @SnowflakeId
    @NotNull

	@Id
	Long id;
  
	/*****
	 * 
	 */
    @DUIField(label = "用户名称")
	@Length(min=1, max=50)
	String userName;
  
	/*****
	 * 
	 */
	@Length(min=1, max=50)
	@NotBlank
	String nickName;

	  
	/*****
	 * 
	 */
	String password;
  
	/*****
	 * 
	 */
	@Length(min=0, max=50)
	@Email
	String email;
  
	/*****
	 * 
	 */
	String mobile;
  
	/*****
	 * 
	 */
	@Enumerated(EnumType.ORDINAL)
    @DUIField(label = "性别")
    @DUISelect(dataEnumClass=UserGenders.class, valueField = "mappingValue")
	UserGenders gender;
	
	@Enumerated(EnumType.STRING)
    @DUIField(label = "用户状态", updatable= true, insertable = false)
    @DUISelect(dataEnumClass=UserStatus.class)
	UserStatus status;

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
	


	public static enum UserGenders implements DbmEnumIntMapping {
		FEMALE("女性", 10),
		MALE("男性", 11);
		
		final private String label;
		final private int value;
		private UserGenders(String label, int value) {
			this.label = label;
			this.value = value;
		}
		public String getLabel() {
			return label;
		}
		@Override
		public int getMappingValue() {
			return value;
		}
	}
}