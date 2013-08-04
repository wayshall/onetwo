package org.onetwo.plugins.permission.entity;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="ADMIN_PAGE_ELEMENT")
@PrimaryKeyJoinColumn(name="PERMISSION_ID")
public class PageElementModel extends PermissionEntity {

	private String name;
	private Long menuId;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public Long getMenuId() {
		return menuId;
	}
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	
}
