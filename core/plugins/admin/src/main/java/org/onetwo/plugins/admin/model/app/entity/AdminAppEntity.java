package org.onetwo.plugins.admin.model.app.entity;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

import org.hibernate.validator.constraints.NotBlank;
import org.onetwo.plugins.admin.utils.WebConstant.ValidGroup.ValidWhenEdit;
import org.onetwo.plugins.admin.utils.WebConstant.ValidGroup.ValidWhenNew;


/*****
 * 用户表
 * @Entity
 */
@SuppressWarnings("serial")
@Entity
@Table(name="ADMIN_APP")
//@TableGenerator(table=com.qyscard.webapp.utils.WebConstant.SEQ_TABLE_NAME, pkColumnName="GEN_NAME",valueColumnName="GEN_VALUE", pkColumnValue="SEQ_ADMIN_APP", allocationSize=50, initialValue=50000, name="UserEntityGenerator")
@Cacheable
public class AdminAppEntity implements Serializable {

	@NotBlank
	@Size(min=1, max=20, groups={Default.class, ValidWhenNew.class, ValidWhenEdit.class})
	private String code;
	@NotBlank
	@Size(min=1, max=100, groups={Default.class, ValidWhenNew.class, ValidWhenEdit.class})
	private String name;
	
	@Id
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}