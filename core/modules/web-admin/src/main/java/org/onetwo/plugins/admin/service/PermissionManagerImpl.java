package org.onetwo.plugins.admin.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.sqlext.ExtQuery.K;
import org.onetwo.common.spring.underline.CopyUtils;
import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.ext.permission.AbstractPermissionManager;
import org.onetwo.ext.permission.api.DataFrom;
import org.onetwo.ext.permission.utils.PermissionUtils;
import org.onetwo.plugins.admin.dao.AdminPermissionDao;
import org.onetwo.plugins.admin.entity.AdminPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class PermissionManagerImpl extends AbstractPermissionManager<AdminPermission> {

    @Autowired
    private BaseEntityManager baseEntityManager;
	
	@Resource
	private AdminPermissionDao adminPermissionDao;
	
	public PermissionManagerImpl() {
	}


//	@Override
	@Transactional
	public AdminPermission getDatabaseRootMenu() {
		return findByCode(this.menuInfoParser.getRootMenuCode());
	}
	
//	@Override
	@Transactional
	public AdminPermission getDatabaseMenuNode(Class<AdminPermission> clazz) {
		String code = menuInfoParser.getCode(clazz);
		return findByCode(code);
	}
	
	
	@Override
    public AdminPermission findByCode(String code) {
		AdminPermission ap = this.baseEntityManager.findById(AdminPermission.class, code);
		if(ap==null)
			return null;
		return ap;
    }
	

	@Override
	protected Map<String, AdminPermission> findExistsPermission(String rootCode) {
		List<AdminPermission> adminPermissions = this.baseEntityManager.findList(AdminPermission.class, "code:like", rootCode+"%");
		Map<String, AdminPermission> dbPermissions = adminPermissions.stream()
													.collect(Collectors.toMap(p->p.getCode(), p->p));
		
		return dbPermissions;
	}


	@Override
	protected void updatePermissions(AdminPermission rootPermission, Map<String, AdminPermission> dbPermissionMap, Set<AdminPermission> adds, Set<AdminPermission> deletes, Set<AdminPermission> updates) {
		logger.info("adds: {}", adds);
		adds.forEach(p->{
			this.baseEntityManager.persist(p);
		});

		logger.info("deletes: {}", deletes);
		deletes.forEach(p->{
			this.baseEntityManager.remove(p);
		});

		logger.info("updates: {}", updates);
		updates.forEach(p->{
//			this.adminPermissionMapper.updateByPrimaryKey(p.getAdminPermission());
			AdminPermission dbPermission = dbPermissionMap.get(p.getCode());
			if(dbPermission.getDataFrom()==DataFrom.SYNC){
//				ReflectUtils.copyIgnoreBlank(dbPermission, p);
				CopyUtils.copier().from(p).ignoreNullValue().ignoreBlankString().to(dbPermission);
				this.baseEntityManager.update(p);
			}
		});
	}


	@Override
	@Transactional(readOnly=true)
	public List<AdminPermission> findAppMenus(String appCode){
//		List<IMenu> menulist = (List<IMenu>)baseEntityManager.findByProperties(this.menuInfoParser.getMenuInfoable().getIMenuClass(), "appCode", appCode);
		List<AdminPermission> permList = findAppPermissions(appCode);
		return permList.stream()
						.filter(p->PermissionUtils.isMenu(p))
						.collect(Collectors.toList());
	}

//	@Override
	@Transactional(readOnly=true)
	public List<AdminPermission> findAppPermissions(String appCode){
		return baseEntityManager.findList(AdminPermission.class, "appCode", appCode, K.ASC, "sort");
	}

//	@Override
	public List<AdminPermission> findPermissionByCodes(String appCode, String[] permissionCodes) {
		return baseEntityManager.findList(AdminPermission.class, "appCode", appCode, "code:in", permissionCodes);
	}

	@Override
	public List<AdminPermission> findUserAppMenus(String appCode, UserDetail userDetail) {
		List<AdminPermission> adminPermissions = this.adminPermissionDao.findAppPermissionsByUserId(appCode, userDetail.getUserId());
		List<AdminPermission> permList = adminPermissions.stream()
				.filter(p->PermissionUtils.isMenu(p))
				.collect(Collectors.toList());
		return permList;
	}

}
