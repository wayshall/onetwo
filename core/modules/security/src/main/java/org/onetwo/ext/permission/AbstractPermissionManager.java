package org.onetwo.ext.permission;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.ext.permission.entity.DefaultIPermission;
import org.onetwo.ext.permission.parser.MenuInfoParser;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;

abstract public class AbstractPermissionManager<P extends DefaultIPermission<P>> implements PermissionManager<P> {

	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	protected Map<String, P> menuNodeMap;

	@Resource
	protected MenuInfoParser<P> menuInfoParser;
	

	/****
	 * 根据menuClass构建菜单
	 */
	@Override
	public void build(){
//		PermissionUtils.setMenuInfoParser(menuInfoParser);
		menuInfoParser.parseTree();
		this.menuNodeMap = menuInfoParser.getPermissionMap();
	}
	

	public P getMemoryRootMenu() {
	    return this.menuInfoParser.getRootMenu();
    }
	
	@Override
	public P getPermission(Class<?> permClass){
		return menuInfoParser.getPermission(permClass);
	}
	
	/***
	 * syncMenuToDatabase菜单同步方法调用时，需要查找已存在的需要同步到菜单数据
	 * @param rootCode
	 * @return
	 */
	abstract protected Map<String, P> findExistsPermission(String rootCode);
	
	/***
	 * syncMenuToDatabase菜单同步方法调用时，通过对比内存和数据库检测需要变更的数据
	 * @param adds
	 * @param deletes
	 * @param updates
	 */
	abstract protected void updatePermissions(P rootPermission, Map<String, P> dbPermissionMap, Set<P> adds, Set<P> deletes, Set<P> updates);
	
	/****
	 * 同步菜单
	 */
	@Override
	@Transactional
	public void syncMenuToDatabase(){
//		Class<?> rootMenuClass = this.menuInfoParser.getMenuInfoable().getRootMenuClass();
//		Class<?> permClass = this.menuInfoParser.getMenuInfoable().getIPermissionClass();
		P rootPermission = this.menuInfoParser.getRootMenu();
//		List<? extends IPermission> permList = (List<? extends IPermission>)this.baseEntityManager.findByProperties(permClass, "code:like", rootCode+"%");
//		Set<P> dbPermissions = findExistsPermission(rootPermission.getCode());
		Map<String, P> dbPermissionMap = findExistsPermission(rootPermission.getCode());
		Set<P> memoryPermissions = new HashSet<P>(menuNodeMap.values());

		Set<P> dbPermissions = new HashSet<>(dbPermissionMap.values());
		Set<P> adds = Sets.difference(memoryPermissions, dbPermissions);
		Set<P> deletes = Sets.difference(dbPermissions, memoryPermissions);
		Set<P> intersections = Sets.intersection(memoryPermissions, dbPermissions);
		
		this.updatePermissions(rootPermission, dbPermissionMap, adds, deletes, intersections);
	}
	

	@Override
	public String parseCode(Class<?> permClass) {
		return menuInfoParser.getCode(permClass);
	}
}
