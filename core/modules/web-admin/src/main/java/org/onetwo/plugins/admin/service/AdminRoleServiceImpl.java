
package org.onetwo.plugins.admin.service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.plugins.admin.dao.AdminPermissionDao;
import org.onetwo.plugins.admin.dao.AdminRoleDao;
import org.onetwo.plugins.admin.entity.AdminPermission;
import org.onetwo.plugins.admin.entity.AdminRole;
import org.onetwo.plugins.admin.entity.AdminUser;
import org.onetwo.plugins.admin.utils.Enums.CommonStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.collect.Sets;

@Service
@Transactional
public class AdminRoleServiceImpl {

    @Autowired
    private BaseEntityManager baseEntityManager;
    @Autowired
    private AdminRoleDao adminRoleDao;
    
    @Autowired
    private AdminPermissionDao adminPermissionDao;
    
    public void findPage(Page<AdminRole> page, AdminRole adminRole){
        baseEntityManager.findPage(AdminUser.class, page);
    }
    
    public List<AdminRole> findByStatus(CommonStatus status, String appCode){
    	return baseEntityManager.findList(AdminRole.class, "status", status, "appCode", appCode);
    }
    
    public void save(AdminRole adminRole){
        Date now = new Date();
        adminRole.setCreateAt(now);
        adminRole.setUpdateAt(now);
        baseEntityManager.save(adminRole);
    }
    
    public AdminRole loadById(Long id){
        return baseEntityManager.load(AdminRole.class, id);
    }
    
    public void update(AdminRole adminRole){
        Assert.notNull(adminRole.getId(), "参数不能为null");
        AdminRole dbAdminRole = loadById(adminRole.getId());
        ReflectUtils.copyIgnoreBlank(adminRole, dbAdminRole);
        dbAdminRole.setUpdateAt(new Date());
        baseEntityManager.update(adminRole);
    }
    
    public void deleteByIds(Long...ids){
        if(ArrayUtils.isEmpty(ids))
            throw new ServiceException("请先选择数据！");
        Stream.of(ids).forEach(id->deleteById(id));
    }
    
    public void deleteById(Long id){
        AdminRole adminRole = loadById(id);
        adminRole.setStatus(CommonStatus.DELETE.name());
        baseEntityManager.update(adminRole);
    }
    
    public List<AdminRole> findRolesByUser(long userId){
    	return this.adminRoleDao.findRolesByUser(userId);
    }
    
    public List<Long> findRoleIdsByUser(long userId){
    	List<AdminRole> roles = findRolesByUser(userId);
    	return roles.stream().map(r->r.getId())
    						 .collect(Collectors.toList());
    }
    
    public void saveUserRoles(long userId, Long[] roleIds){
    	this.adminRoleDao.deleteUserRoles(userId);
    	Stream.of(roleIds).forEach(roleId->adminRoleDao.insertUserRole(userId, roleId));
    }
    

    public List<String> findAppPermissionCodesByRoleIds(String appCode, long roleId){
    	AdminRole role = loadById(roleId);
    	if(CommonStatus.valueOf(role.getStatus())==CommonStatus.DELETE){
    		throw new ServiceException("角色已删除，不能分配权限");
    	}
    	List<AdminPermission> perms = this.adminPermissionDao.findAppPermissionsByRoleIds(appCode, roleId);
    	return perms.stream().map(p->p.getCode())
    						 .collect(Collectors.toList());
	}
    
    public void saveRolePermission(long roleId, String...assignPerms){
    	if(LangUtils.isEmpty(assignPerms)){
    		this.adminRoleDao.deleteRolePermisssion(null, roleId, null);
    		return ;
    	}
    	List<String> existsPermCodes = this.adminRoleDao.findRolePermisssion(null, roleId);
    	
    	Set<String> adds = Sets.difference(Sets.newHashSet(assignPerms), Sets.newHashSet(existsPermCodes));
    	adds.stream().forEach(code->adminRoleDao.insertRolePermission(roleId, code));
    	
		Set<String> deletes = Sets.difference(Sets.newHashSet(existsPermCodes), Sets.newHashSet(assignPerms));
		deletes.stream().forEach(code->adminRoleDao.deleteRolePermisssion(null, roleId, code));
    }
	
}