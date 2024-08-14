package org.onetwo.common.web.userdetails;

import java.util.List;

//@Deprecated
public interface RoleDetail extends UserDetail {
	String SYSTEM_ROOT_ROLE_CODE = "ROOT";
//	Long SYSTEM_ROOT_ROLE_ID = 1L;
	
	List<String> getRoles();
	
//	public boolean isSystemRootRole();
	
	/***
	 * 是否普通用户角色
	 * @author weishao zeng
	 * @return
	 */
	default boolean isCommonUserRole() {
		return true;
	}

}
