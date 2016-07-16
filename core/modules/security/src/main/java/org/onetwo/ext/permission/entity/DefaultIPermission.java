package org.onetwo.ext.permission.entity;

import org.onetwo.common.utils.func.Closure1;
import org.onetwo.ext.permission.api.IPermission;
import org.onetwo.ext.permission.utils.PermissionUtils;


public interface DefaultIPermission<T extends DefaultIPermission<T>> extends IPermission<T> {
	
	@SuppressWarnings("unchecked")
	default public String toTreeString(String spliter){
		final StringBuilder str = new StringBuilder();
		PermissionUtils.buildString(str, (T)this, "--", new Closure1<T>() {
			
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