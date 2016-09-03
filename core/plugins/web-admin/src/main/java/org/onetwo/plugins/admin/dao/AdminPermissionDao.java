
package org.onetwo.plugins.admin.dao;

import java.util.Collection;
import java.util.List;

import org.onetwo.common.db.dquery.annotation.Param;
import org.onetwo.plugins.admin.entity.AdminPermission;


public interface AdminPermissionDao {

	List<AdminPermission> findAppPermissionsByUserId(@Param("appCode")String appCode, @Param("userId")long userId);
	List<AdminPermission> findAppPermissions(@Param("appCode")String appCode);
	List<AdminPermission> findAppPermissionsByRoleIds(@Param("appCode")String appCode, @Param("roleId")long roleId);
	

	List<AdminPermission> findPermissions(@Param("codes")Collection<Object> codes);
	
}