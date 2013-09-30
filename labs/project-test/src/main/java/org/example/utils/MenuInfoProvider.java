package org.example.utils;

import org.onetwo.plugins.permission.PermissionConfig;
import org.springframework.stereotype.Component;

@Component
public class MenuInfoProvider implements PermissionConfig {

	@Override
	public Class<?> getRootMenuClass() {
		return MenuInfo.class;
	}

}
