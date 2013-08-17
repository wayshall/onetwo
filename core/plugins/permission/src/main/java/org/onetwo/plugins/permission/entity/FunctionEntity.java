package org.onetwo.plugins.permission.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.onetwo.common.db.IBaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name="ADMIN_FUNCTION")
//@PrimaryKeyJoinColumn(name="PERMISSION_ID")
//@DiscriminatorValue("FUNCTION")
public class FunctionEntity extends PermissionEntity implements IBaseEntity{

	private MenuEntity menu;

	private Date createTime;
	private Date lastUpdateTime;

	public FunctionEntity(){
		this.setPtype(PermissionType.FUNCTION);
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
