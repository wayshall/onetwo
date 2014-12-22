package org.onetwo.plugins.admin.model.app.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.onetwo.plugins.permission.entity.IFunction;
import org.onetwo.plugins.permission.entity.PermissionType;

@SuppressWarnings("serial")
@Entity
@Table(name="ADMIN_FUNCTION")
//@PrimaryKeyJoinColumn(name="PERMISSION_ID")
//@DiscriminatorValue("FUNCTION")
public class AdminFunctionEntity extends AdminPermissionEntity implements IFunction<AdminMenuEntity>{

	private AdminMenuEntity menu;

	private Date createTime;
	private Date lastUpdateTime;

	public AdminFunctionEntity(){
		this.setPtype(PermissionType.FUNCTION);
	}
	
	@Override
	@ManyToOne
	@JoinColumn(name="MENU_CODE")
	public AdminMenuEntity getMenu() {
		return menu;
	}

	@Override
	public void setMenu(AdminMenuEntity menu) {
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
