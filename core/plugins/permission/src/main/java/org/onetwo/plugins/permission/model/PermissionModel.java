package org.onetwo.plugins.permission.model;

import java.util.List;

import javax.persistence.Transient;

@SuppressWarnings("serial")
//@Entity
//@Table(name="ADMIN_PERMISSIONS")
public class PermissionModel extends BaseModel {
	
	private String name;
	private List<ResourceModel> accessableResources;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Transient
	public List<ResourceModel> getAccessableResources() {
		return accessableResources;
	}
	public void setAccessableResources(List<ResourceModel> accessableResources) {
		this.accessableResources = accessableResources;
	}

	
}
