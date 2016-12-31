package projects.manager.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.onetwo.plugins.admin.entity.AdminUser;
import org.onetwo.plugins.admin.utils.DataUtils;

import projects.manager.utils.Enums.UserTypes;

@Entity
@Table(name="admin_user")
@Data
@EqualsAndHashCode(callSuper=true)
public class User extends AdminUser {
	
	private Long belongToUserId;
	private Integer userType;
	private String qq;
	private String bank;
	private String bankAccount;

	public String getBankName(){
		return DataUtils.getDictionaryCachingService().findName(bank);
	}
	@Transient
	public String getUserTypeName(){
		return UserTypes.of(userType).getLabel();
	}

}