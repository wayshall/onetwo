package org.onetwo.boot.plugins.permission.entity;

import java.util.List;

import org.onetwo.boot.plugins.permission.utils.PermissionUtils;
import org.onetwo.common.utils.Closure;

public interface IPermission<T extends IPermission<T>> {

	String getCode();

	void setCode(String code);

	/*String getPtype();

	void setPtype(String ptype);*/

	public PermissionType getPermissionType();
	
	public void setPermissionType(PermissionType type);
	
	String getUrl();

	void setUrl(String url);

	String getMethod();

	void setMethod(String method);

	String getParentCode();

	void setParentCode(String parentCode);

	String getName();

	void setName(String name);

	Integer getSort();

	void setSort(Integer sort);
	
	public String getAppCode();

	public void setAppCode(String appCode);

	boolean isHidden();

	void setHidden(boolean hidden);
	public List<T> getChildrenPermissions();

	void setChildrenSize(int childrenSize);
	int getChildrenSize();
//	public void setChildrenPermissions(List<? extends IPermission> childrenPermissions);

	public void addChild(T permission);

	public void addChildren(T... permissions);

	public List<T> getChildrenMenu();
	public List<T> getChildrenWithouMenu();
	
	public String getResourcesPattern();
	public void setResourcesPattern(String resources);
	
	public DataFrom getDataFrom();
	public void setDataFrom(DataFrom dataFrom);
	

	default public String toTreeString(String spliter){
		final StringBuilder str = new StringBuilder();
		PermissionUtils.buildString(str, (T)this, "--", new Closure<T>() {
			
			@Override
			public void execute(T obj) {
				if(PermissionUtils.isMenu(obj)){
					str.append(obj.getName()).append("(").append(obj.getCode()).append(")");
					str.append(":").append(obj.getUrl()==null?"":obj.getUrl() );
				}else{
					str.append(obj.getName()).append("(").append(obj.getCode()).append(")");
				}
				str.append(" -> ").append(obj.getResourcesPattern()==null?"":obj.getResourcesPattern() );
				str.append(spliter);
			}
		});
		return str.toString();
	}
}