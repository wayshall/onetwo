package org.onetwo.plugins.permission.entity;


public interface IPermission {
//	public Long getId();
//	public void setId(Long id);

	public Boolean isHidden();
	public void setHidden(Boolean hidden);
	
	public Integer getSort();
	public void setSort(Integer sort);
	public String getName();
	public void setName(String name);
//	public void setSyscode(String syscode);
	String getCode();
	void setCode(String code);
	
	public void onRemove();

//	PermissionType getPtype();

//	void setPtype(PermissionType ptype);

}