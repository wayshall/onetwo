package org.onetwo.ext.permission.api;

import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.func.Closure1;
import org.onetwo.ext.permission.utils.PermissionUtils;

public interface IPermission {

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
	public List<IPermission> getChildrenPermissions();

	void setChildrenSize(int childrenSize);
	int getChildrenSize();
//	public void setChildrenPermissions(List<? extends IPermission> childrenPermissions);

	public void addChild(IPermission permission);

	public void addChildren(IPermission... permissions);

	public List<IPermission> getChildrenMenu();
	public List<IPermission> getChildrenWithouMenu();
	
	public String getResourcesPattern();
	public void setResourcesPattern(String resources);
	
	public DataFrom getDataFrom();
	public void setDataFrom(DataFrom dataFrom);
	
	Map<String, Object> getMeta();
	void setMeta(Map<String, Object> meta);

	default public String toTreeString(String spliter){
		final StringBuilder str = new StringBuilder();
		PermissionUtils.buildString(str, (IPermission)this, "--", new Closure1<IPermission>() {
			
			@Override
			public void execute(IPermission obj) {
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