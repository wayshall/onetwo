package org.onetwo.plugins.permission.entity;


public interface IPermission {
	public Long getId();
	public void setId(Long id);
	public String getName();
	public void setName(String name);
	String getCode();

	void setCode(String code);

	PermissionType getPtype();

	void setPtype(PermissionType ptype);

}