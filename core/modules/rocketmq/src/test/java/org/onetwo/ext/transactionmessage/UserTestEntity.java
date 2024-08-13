package org.onetwo.ext.transactionmessage;

import org.hibernate.validator.constraints.Length;
import org.onetwo.dbm.annotation.SnowflakeId;
import org.onetwo.dbm.jpa.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wayshall
 * <br/>
 */
@Entity
@Table(name="TEST_USER")
@Data
@EqualsAndHashCode(callSuper = false)
public class UserTestEntity extends BaseEntity {

	@SnowflakeId
	protected Long id;
	@Length(min=1, max=50)
	protected String userName;
	protected String email;
	protected String mobile;
	protected String nickName;
	protected Integer gender;
	protected UserStatus status;
	
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
}
