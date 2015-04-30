package appweb.admin.utils;

import org.onetwo.plugins.admin.model.app.entity.AdminFunctionEntity;
import org.onetwo.plugins.admin.model.app.entity.AdminMenuEntity;
import org.onetwo.plugins.admin.model.app.entity.AdminPermissionEntity;
import org.onetwo.plugins.permission.PermissionConfig;
import org.springframework.stereotype.Component;

@Component
public class MenuInfoProvider implements PermissionConfig {
	

	@Override
	public Class<?> getRootMenuClass() {
		return AppwebAdmin.class;
	}

	@Override
	public Class<AdminMenuEntity> getIMenuClass() {
		return AdminMenuEntity.class;
	}

	@Override
	public Class<AdminFunctionEntity> getIFunctionClass() {
		return AdminFunctionEntity.class;
	}

	@Override
	public Class<AdminPermissionEntity> getIPermissionClass() {
		return AdminPermissionEntity.class;
	}

	@Override
	public String[] getChildMenuPackages() {
		return new String[]{"qingxinkd"};
	}
	

}
