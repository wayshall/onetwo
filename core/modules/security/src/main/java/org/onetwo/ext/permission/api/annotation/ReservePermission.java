package org.onetwo.ext.permission.api.annotation;

/***
 * 是否security保留权限，如：fullyAuthenticated, permitAll,denyAll等
 * @author way
 *
 */
public interface ReservePermission {

	String authCode();
	
}
