package org.onetwo.plugins.permission;

import org.onetwo.common.web.view.ViewPermission;

public class ViewPermissionImpl implements ViewPermission {

	@Override
	public boolean hasPermission(String code) {
		return PermissionUtils.hasPermission(code);
	}

}
