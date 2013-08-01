package org.onetwo.plugins.permission.model;

@SuppressWarnings("serial")
public class ElementModel extends BaseModel {

	private String name;
	private PermissionModel ownerPermission;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public PermissionModel getOwnerPermission() {
		return ownerPermission;
	}
	public void setOwnerPermission(PermissionModel ownerPermission) {
		this.ownerPermission = ownerPermission;
	}
	
}
