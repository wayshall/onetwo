package org.onetwo.ext.permission.api.annotation;

import org.onetwo.ext.permission.api.PermissionType;

/**
 * 特殊的保留权限，用于默认权限，表示已登录认证
 * 此权限不会显示在菜单权限分配列表（被过滤）
 * 也不会在同步的时候被移除
 * @author weishao zeng
 * @see org.springframework.security.web.access.expression.WebSecurityExpressionRoot
 * @see org.springframework.security.access.expression.method.MethodSecurityExpressionRoot
 * <br/>
 */
@PermissionMeta(name = "已登录认证", permissionType = PermissionType.FUNCTION, hidden = true)
public interface FullyAuthenticated extends ReservePermission {
	String AUTH_CODE = "fullyAuthenticated";
	
	default String authCode() {
		return AUTH_CODE;
	}
}
