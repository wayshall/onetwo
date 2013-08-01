package org.onetwo.plugins.permission.model;

import java.util.List;

@SuppressWarnings("serial")
public class MenuModel extends BaseModel {

	private String name;
	private String url;
	private MenuModel parent;

	private PermissionModel ownerPermission;
	private List<ElementModel> pageElement;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public MenuModel getParent() {
		return parent;
	}

	public void setParent(MenuModel parent) {
		this.parent = parent;
	}

	public PermissionModel getOwnerPermission() {
		return ownerPermission;
	}

	public void setOwnerPermission(PermissionModel ownerPermission) {
		this.ownerPermission = ownerPermission;
	}

	public List<ElementModel> getPageElement() {
		return pageElement;
	}

	public void setPageElement(List<ElementModel> pageElement) {
		this.pageElement = pageElement;
	}

}
