package org.onetwo.plugins.permission.service;

import org.onetwo.common.web.view.ViewPermission;
import org.onetwo.plugins.permission.PermissionUtils;

public class ViewPermissionImpl implements ViewPermission {

	@Override
	public boolean hasPermission(String code) {
		return PermissionUtils.hasPermission(code);
	}

}
