package org.onetwo.common.web.userdetails;

import java.util.List;

//@Deprecated
public interface RoleDetail {
	String SYSTEM_ROOT_ROLE_CODE = "ROOT";
//	Long SYSTEM_ROOT_ROLE_ID = 1L;
	
	public List<String> getRoles();
	
//	public boolean isSystemRootRole();

}
