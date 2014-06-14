package org.onetwo.plugins.permission.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.log.MyLoggerFactory;
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
		IMenu rootMenu = menuInfoParser.parseTree();
//		logger.info("menu:\n" + rootMenu);
		this.menuNodeMap = menuInfoParser.getPermissionMap();
	}
	
	@Override
	public IPermission getPermission(Class<?> permClass){
		return menuInfoParser.getPermission(permClass);
	}

	@Override
	@Transactional
	public <T extends IMenu> T getDatabaseRootMenu() {
		return (T)baseEntityManager.findUnique(IMenu.class, "code", this.menuInfoParser.getRootMenuCode());
	}
	
	@Override
	@Transactional
	public <T extends IMenu> T getDatabaseMenuNode(Class<?> clazz) {
		String code = menuInfoParser.parseCode(clazz);
		return (T)baseEntityManager.findUnique(this.menuInfoParser.getMenuInfoable().getIMenuClass(), "code", code);
	}
	
	/****
	 * 同步菜单
	 */
	@Override
	@Transactional
	public void syncMenuToDatabase(){
		Class<?> rootMenuClass = this.menuInfoParser.getMenuInfoable().getRootMenuClass();
		Class<?> permClass = this.menuInfoParser.getMenuInfoable().getIPermissionClass();
		List<? extends IPermission> permList = (List<? extends IPermission>)this.baseEntityManager.findByProperties(permClass, "code:like", rootMenuClass.getSimpleName()+"%");
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
	
/*	
	public static class RescourceTreeModel implements TreeModel<RescourceTreeModel> {

		private final JResourceInfo info;
		private final RescourceTreeModel parent;
		private List<RescourceTreeModel> children;
		
		public RescourceTreeModel(JResourceInfo info) {
			super();
			this.info = info;
			RescourceTreeModel parent = null;
			if(info.getParent()!=null)
				parent = new RescourceTreeModel(info.getParent());
			this.parent = parent;
		}

		@Override
		public void addChild(RescourceTreeModel node) {
			info.addChild(node.getInfo());
			if(children==null)
				children = LangUtils.newArrayList();
			this.children.add(node);
		}

		@Override
		public Object getParentId() {
			if(info.getParent()==null)
				return null;
			return info.getParent().getId();
		}

		@Override
		public Object getId() {
			return info.getId();
		}

		@Override
		public String getName() {
			return info.getLabel();
		}

		@Override
		public Comparable<?> getSort() {
			return info.getId();
		}

		public List<RescourceTreeModel> getChildren() {
			return children;
		}

		public JResourceInfo getInfo() {
			return info;
		}
		
		public RescourceTreeModel getParent(){
			return parent;
		}
		
		public String toString(){
			StringBuilder str = new StringBuilder();
			TreeUtils.buildString(str, this, "--");
			return str.toString();
		}
	}*/


}
