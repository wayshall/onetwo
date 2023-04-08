package org.onetwo.ext.permission.api;

import org.springframework.security.access.expression.SecurityExpressionRoot;

/***
 * 常用权限代码
 * 
 * 权限判断参考下面两个类：
 * @see org.springframework.security.access.expression.method.MethodSecurityExpressionRoot
 * @see SecurityExpressionRoot
 * @author way
 *
 */
public interface Permissions {

	/***
	 * 验证是否已登录
	 */
	String FULLY_AUTHENTICATED = "fullyAuthenticated";
	
	/****
	 * 是否管理员
	 * 使用hasRole判断时，用户的角色必须添加ROLE_前缀；
	 * 使用hasAnyAuthority判断时，则不需要
	 */
	String ADMIN = "hasRole('ROLE_ADMIN') || hasAnyAuthority('ADMIN')";
	/****
	 * 企业用户
	 */
	String ENT_USER = "hasRole('ROLE_ENT_USER') || hasAnyAuthority('ENT_USER')";
	
	String ROOT = "principal.systemRootUser";

}
