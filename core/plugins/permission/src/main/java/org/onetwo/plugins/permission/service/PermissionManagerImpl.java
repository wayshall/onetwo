package org.onetwo.plugins.permission.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.db.ExtQuery.K.IfNull;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.Assert;
import org.onetwo.plugins.permission.MenuInfoParser;
import org.onetwo.plugins.permission.PermissionUtils;
import org.onetwo.plugins.permission.entity.IMenu;
import org.onetwo.plugins.permission.entity.IPermission;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class PermissionManagerImpl implements PermissionManager {
	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());

	private Map<String, ? extends IPermission> menuNodeMap;

	@Resource
	private MenuInfoParser menuInfoParser;
	
	@Resource
	private BaseEntityManager baseEntityManager;
	
	public PermissionManagerImpl() {
	}

	@Override
	public void build(){
		PermissionUtils.setMenuInfoParser(menuInfoParser);
//		IMenu rootMenu = menuInfoParser.parseTree();
//		logger.info("menu:\n" + rootMenu);
		this.menuNodeMap = menuInfoParser.getPermissionMap();
	}
	
	@Override
	public IPermission getPermission(Class<?> permClass){
		return menuInfoParser.getPermission(permClass);
	}

	@Override
	@Transactional
	public IMenu getDatabaseRootMenu() {
		return (IMenu)baseEntityManager.findUnique(IMenu.class, "code", this.menuInfoParser.getRootMenuCode());
	}
	
	@Override
	@Transactional
	public IMenu getDatabaseMenuNode(Class<?> clazz) {
		String code = menuInfoParser.parseCode(clazz);
		return (IMenu)baseEntityManager.findUnique(this.menuInfoParser.getMenuInfoable().getIMenuClass(), "code", code);
	}
	
	/****
	 * 同步菜单
	 */
	@Override
	@Transactional
	public void syncMenuToDatabase(){
		Class<?> rootMenuClass = this.menuInfoParser.getMenuInfoable().getRootMenuClass();
		Class<?> permClass = this.menuInfoParser.getMenuInfoable().getIPermissionClass();
		String rootCode = parseCode(rootMenuClass);
		List<? extends IPermission> permList = (List<? extends IPermission>)this.baseEntityManager.findByProperties(permClass, "code:like", rootCode+"%");
//		Map<String, IPermission> mapByCode = index(permList, on(IPermission.class).getCode());
		
		Session session = baseEntityManager.getRawManagerObject(SessionFactory.class).getCurrentSession();
		for(IPermission dbperm : permList){
			IPermission clsPerm = menuNodeMap.get(dbperm.getCode());
			if(!session.contains(dbperm))
				continue;
			if(clsPerm==null){
				removePermission(dbperm);
				session.evict(dbperm);
			}else if(clsPerm.getClass()!=dbperm.getClass()){
				removePermission(dbperm);
				session.evict(dbperm);
			}
		}
		session.flush();
		session.merge(this.menuInfoParser.getRootMenu());
	}
	
	/*@Transactional
	public void syncMenuToDatabase2(){
		baseEntityManager.save(this.menuInfoParser.getRootMenu());
		Class<?> permClass = this.menuInfoParser.getMenuInfoable().getIPermissionClass();
		List<? extends IPermission> permList = (List<? extends IPermission>)this.baseEntityManager.findAll(permClass);
		Map<String, IPermission> mapByCode = index(permList, on(IPermission.class).getCode());
		
		int menuCount = 0;
		for(IPermission perm : menuNodeMap.values()){
			logger.info("sync perm[{}]...", perm.getCode());
//			IPermission dbperm = (IPermission) this.baseEntityManager.findUnique(permClass, "code", perm.getCode());
			IPermission dbperm = mapByCode.remove(perm.getCode());
			
			if(dbperm!=null){
				if(perm.getClass()!=dbperm.getClass()){
					removePermission(dbperm);
//					Long id = this.baseEntityManager.getSequences(this.menuInfoParser.getMenuInfoable().getIPermissionClass(), false);
//					perm.setId(id);
//					baseEntityManager.save(perm);
				}
			}

			baseEntityManager.save(perm);
			menuCount++;
		}
		logger.info("sync menu count: {}", menuCount);
//		List<Long> ids = extract(menuNodeMap.values(), on(IPermission.class).getId());
		List<String> ids = extract(menuNodeMap.values(), on(IPermission.class).getCode());
		ExtQuery deleteq = baseEntityManager.getSQLSymbolManager().createDeleteQuery(menuInfoParser.getMenuInfoable().getIPermissionClass(), LangUtils.asMap("code:not in", ids));
		deleteq.build();
		DataQuery dq = baseEntityManager.createQuery(deleteq.getSql(), deleteq.getParamsValue().asMap());
		int deleteCount = dq.executeUpdate();
		if(!mapByCode.isEmpty()){
			for(IPermission perm : mapByCode.values()){
				removePermission(perm);
			}
			logger.info("delete {} menu", mapByCode.size());
		}
	}*/
	
	private void removePermission(IPermission dbperm){
		dbperm.onRemove();
		baseEntityManager.remove(dbperm);
	}
	
	@Override
	public <T> T findById(Long id){
		return (T)baseEntityManager.findById(this.menuInfoParser.getMenuInfoable().getIPermissionClass(), id);
	}

	public MenuInfoParser getMenuInfoParser() {
		return menuInfoParser;
	}

	@Override
	public String parseCode(Class<?> permClass) {
		return menuInfoParser.parseCode(permClass);
	}

	@Override
	@Transactional(readOnly=true)
	public List<IMenu> findAppMenus(String appCode){
		List<IMenu> menulist = (List<IMenu>)baseEntityManager.findByProperties(this.menuInfoParser.getMenuInfoable().getIMenuClass(), "appCode", appCode);
		return menulist;
	}

	@Override
	@Transactional(readOnly=true)
	public List<? extends IPermission> findAppPermissions(String appCode){
		List<IPermission> menulist = (List<IPermission>)baseEntityManager.findByProperties(this.menuInfoParser.getMenuInfoable().getIPermissionClass(), "appCode", appCode);
		return menulist;
	}

	@Override
	public List<? extends IPermission> findPermissionByCodes(String appCode, String[] permissionCodes) {
		Assert.notEmpty(permissionCodes);
		List<IPermission> permlist = (List<IPermission>)baseEntityManager.findByProperties(
																	this.menuInfoParser.getMenuInfoable().getIPermissionClass(), 
																	"code:in", permissionCodes,
																	"appCode", appCode,
																	K.IF_NULL, IfNull.Ignore);
		return permlist;
	}
	
	

}
