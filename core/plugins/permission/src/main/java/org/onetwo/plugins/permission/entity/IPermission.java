package org.onetwo.plugins.permission.entity;


public interface IPermission {
//	public Long getId();
//	public void setId(Long id);
	public Integer getSort();
	public void setSort(Integer sort);
	public String getName();
	public void setName(String name);
	String getCode();
	void setCode(String code);
	
	public void removeRelations();

//	PermissionType getPtype();

//	void setPtype(PermissionType ptype);

}