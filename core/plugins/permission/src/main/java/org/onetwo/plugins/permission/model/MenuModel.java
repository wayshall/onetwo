package org.onetwo.plugins.permission.model;

import java.util.List;

public class MenuModel extends BaseModel{

	private String url;
	private MenuModel parent;
	
	private PermissionModel ownerPermission;
	private List<ElementModel> pageElement;
}
