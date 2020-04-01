package org.onetwo.ext.permission.api.annotation;

import org.onetwo.ext.permission.api.PermissionType;

/**
 * @author weishao zeng
 * <br/>
 */

@PermissionMeta(name = "已登录认证", permissionType = PermissionType.FUNCTION, hidden = true)
public interface FullyAuthenticated {
	String AUTH_CODE = "fullyAuthenticated";
}
