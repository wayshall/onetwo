package org.onetwo.webapp.manager.utils;

import org.onetwo.ext.permission.AbstractPermissionConfig;
import org.onetwo.plugins.admin.entity.AdminPermission;
import org.springframework.stereotype.Component;

@Component
public class MenuConfig extends AbstractPermissionConfig<AdminPermission> {

	@Override
    public Class<?> getRootMenuClass() {
	    return AppPerms.class;
    }

	@Override
    public String[] getChildMenuPackages() {
	    return null;
    }

	@Override
    public Class<AdminPermission> getIPermissionClass() {
	    return AdminPermission.class;
    }
	
	

}
