package org.onetwo.plugins.admin.model.user.entity;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


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
	
	private String code;
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