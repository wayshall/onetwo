package org.onetwo.common.web.userdetails;

import java.util.List;

public interface RoleIdDetail {
	Long SYSTEM_ROOT_ROLE_ID = 1L;
	
	public List<Long> getRoleIds();
	

}
