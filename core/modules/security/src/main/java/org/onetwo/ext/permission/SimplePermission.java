package org.onetwo.ext.permission;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.permission.api.DataFrom;
import org.onetwo.ext.permission.api.IPermission;
import org.onetwo.ext.permission.api.PermissionType;
import org.onetwo.ext.permission.utils.PermissionUtils;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class SimplePermission implements Serializable, IPermission {
	
	private String code;

    private String ptype;

    private DataFrom dataFrom;

    private String url;

    private String method;

    private String parentCode;

    private String name;

    private Integer sort;

    private boolean hidden;

    private int childrenSize;

    private String appCode;

    private String resourcesPattern;
    
    private Map<String, Object> meta;
    
	private List<IPermission> childrenPermissions = LangUtils.newArrayList();


	public PermissionType getPermissionType(){
		return PermissionType.of(getPtype());
	}
	
	public void setPermissionType(PermissionType type){
		this.setPtype(type.name());
	}

	@Override
	public List<IPermission> getChildrenPermissions() {
		return childrenPermissions;
	}

	@Override
	public void addChild(IPermission permission) {
		childrenPermissions.add(permission);
	}

	@Override
	public void addChildren(IPermission... permissions) {
		childrenPermissions.addAll(Arrays.asList(permissions));
	}

	@Override
	public List<IPermission> getChildrenMenu() {
		if(childrenPermissions==null)
			return Collections.emptyList();
		return childrenPermissions.stream()
				.filter(p->PermissionUtils.isMenu(p))
				.collect(Collectors.toList());
	}

	@Override
	public List<IPermission> getChildrenWithouMenu() {
		if(childrenPermissions==null)
			return Collections.emptyList();
		return childrenPermissions.stream()
				.filter(p->!PermissionUtils.isMenu(p))
				.collect(Collectors.toList());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimplePermission other = (SimplePermission) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}


}
