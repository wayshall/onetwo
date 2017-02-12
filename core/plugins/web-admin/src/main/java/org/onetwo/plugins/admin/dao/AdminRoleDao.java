
package org.onetwo.plugins.admin.dao;

import java.util.List;

import org.onetwo.common.db.dquery.annotation.DbmRepository;
import org.onetwo.common.db.dquery.annotation.Param;
import org.onetwo.plugins.admin.entity.AdminRole;

@DbmRepository
public interface AdminRoleDao {
    
	public List<AdminRole> findRolesByUser(@Param("userId")long userId);
	
	public int deleteUserRoles(@Param("userId")long userId);
	public int insertUserRole(@Param("userId")long userId, @Param("roleId")long roleId); 
	
	public int insertRolePermission(@Param("roleId")long roleId, @Param("permissionCode")String permissionCode);
	public int deleteRolePermisssion(@Param("appCode")String appCode, @Param("roleId")long roleId, @Param("permissionCode")String permissionCode);
	public List<String> findRolePermisssion(@Param("appCode")String appCode, @Param("roleId")long roleId);
	
}