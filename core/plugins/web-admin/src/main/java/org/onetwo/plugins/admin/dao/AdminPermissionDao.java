
package org.onetwo.plugins.admin.dao;

import java.util.Collection;
import java.util.List;

import org.onetwo.common.db.dquery.annotation.DbmRepository;
import org.onetwo.common.db.dquery.annotation.Param;
import org.onetwo.plugins.admin.entity.AdminPermission;

@DbmRepository
public interface AdminPermissionDao {

	List<AdminPermission> findAppPermissionsByUserId(@Param("appCode")String appCode, @Param("userId")long userId);
	List<AdminPermission> findAppPermissionsByRoleIds(@Param("appCode")String appCode, @Param("roleId")long roleId);
	
	/***
	 * 根据appcode查找权限
	 * @author wayshall
	 * @param appCode
	 * @return
	 */
	List<AdminPermission> findAppPermissions(@Param("appCode")String appCode);
	/***
	 * 根据多个appcode查找权限
	 * @author wayshall
	 * @param codes
	 * @return
	 */
	List<AdminPermission> findPermissions(@Param("codes")Collection<String> codes);
	
	int deleteRolePermissions(@Param("permissionCode")String permissionCode);
	
}