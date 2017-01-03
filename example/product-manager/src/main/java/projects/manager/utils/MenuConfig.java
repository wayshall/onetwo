package projects.manager.utils;

import org.onetwo.ext.permission.PermissionConfigAdapter;
import org.onetwo.plugins.admin.entity.AdminPermission;
import org.springframework.stereotype.Component;

@Component
public class MenuConfig extends PermissionConfigAdapter<AdminPermission> {

	@Override
    public Class<?> getRootMenuClass() {
	    return Products.class;
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
