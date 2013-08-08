package org.onetwo.plugins.permission.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.onetwo.common.db.IBaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name="ADMIN_PAGE_ELEMENT")
//@PrimaryKeyJoinColumn(name="PERMISSION_ID")
//@DiscriminatorValue("PAGE")
public class PageElementEntity extends PermissionEntity implements IBaseEntity{

	private String name;
	private MenuEntity menu;

	private Date createTime;
	private Date lastUpdateTime;

	public PageElementEntity(){
		this.setPtype("PAGE");
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	@ManyToOne
	@JoinColumn(name="MENU_ID")
	public MenuEntity getMenu() {
		return menu;
	}

	public void setMenu(MenuEntity menu) {
		this.menu = menu;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
}
