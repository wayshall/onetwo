
package org.onetwo.plugins.admin.dao;

import java.util.List;

import org.onetwo.common.db.dquery.annotation.AsCountQuery;
import org.onetwo.common.db.dquery.annotation.DbmRepository;
import org.onetwo.common.db.dquery.annotation.Param;
import org.onetwo.common.db.dquery.annotation.Query;
import org.onetwo.plugins.admin.entity.AdminRole;

@DbmRepository
public interface AdminRoleDao {
    
	List<AdminRole> findRolesByUser(@Param("userId")long userId);
	
	int deleteUserRoles(@Param("userId")long userId);
	int insertUserRole(@Param("userId")long userId, @Param("roleId")long roleId); 
	
	int insertRolePermission(@Param("roleId")long roleId, @Param("permissionCode")String permissionCode);
	int deleteRolePermisssion(@Param("appCode")String appCode, @Param("roleId")long roleId, @Param("permissionCode")String permissionCode);
	List<String> findRolePermisssion(@Param("appCode")String appCode, @Param("roleId")long roleId);
	
	@AsCountQuery("findRolePermisssion")
	int countRolePermisssion(@Param("appCode")String appCode, @Param("roleId")long roleId);
	
	@Query(value="SELECT COUNT(1) FROM admin_user_role ur where ur.ROLE_ID=:roleId")
	int countUserRole(@Param("roleId")long roleId);
	
}