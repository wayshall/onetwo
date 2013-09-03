package org.onetwo.plugins.permission.service;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.on;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.ExtQuery;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.permission.MenuInfoParser;
import org.onetwo.plugins.permission.PermissionUtils;
import org.onetwo.plugins.permission.entity.IMenu;
import org.onetwo.plugins.permission.entity.IPermission;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class PermissionManagerImpl {
	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());

	private Map<String, ? extends IPermission> menuNodeMap;

	@Resource
	private MenuInfoParser menuInfoParser;
	
	@Resource
	private BaseEntityManager baseEntityManager;
	
	public PermissionManagerImpl() {
	}

	public void build(){
		PermissionUtils.setMenuInfoParser(menuInfoParser);
		IMenu rootMenu = menuInfoParser.parseTree();
		logger.info("menu:\n" + rootMenu);
		this.menuNodeMap = menuInfoParser.getMenuNodeMap();
	}
	

	@Transactional
	public <T extends IMenu> T getDatabaseRootMenu() {
		return (T)baseEntityManager.findUnique(IMenu.class, "code", this.menuInfoParser.getRootMenuCode());
	}
	
	@Transactional
	public <T extends IMenu> T getDatabaseMenuNode(Class<?> clazz) {
		String code = menuInfoParser.parseCode(clazz);
		return (T)baseEntityManager.findUnique(this.menuInfoParser.getMenuInfoable().getIMenuClass(), "code", code);
	}
	
	@Transactional
	public void syncMenuToDatabase(){
		Class<?> permClass = this.menuInfoParser.getMenuInfoable().getIPermissionClass();
		List<? extends IPermission> permList = (List<? extends IPermission>)this.baseEntityManager.findAll(permClass);
		Map<String, IPermission> mapByCode = index(permList, on(IPermission.class).getCode());
		
		int menuCount = 0;
		for(IPermission perm : menuNodeMap.values()){
			logger.info("sync perm[{}]...", perm.getCode());
//			IPermission dbperm = (IPermission) this.baseEntityManager.findUnique(permClass, "code", perm.getCode());
			IPermission dbperm = mapByCode.get(perm.getCode());
			if(dbperm!=null){
				if(perm.getClass()!=dbperm.getClass()){
					baseEntityManager.remove(dbperm);
					Long id = this.baseEntityManager.getSequences(this.menuInfoParser.getMenuInfoable().getIPermissionClass(), false);
					perm.setId(id);
					baseEntityManager.save(perm);
				}else{
					perm.setId(dbperm.getId());
					baseEntityManager.merge(perm);
				}
			}else{
				Long id = this.baseEntityManager.getSequences(this.menuInfoParser.getMenuInfoable().getIPermissionClass(), false);
				perm.setId(id);
				baseEntityManager.persist(perm);
			}
			menuCount++;
		}
		logger.info("sync menu count: {}", menuCount);
		List<Long> ids = extract(menuNodeMap.values(), on(IPermission.class).getId());
		ExtQuery deleteq = baseEntityManager.getSQLSymbolManager().createDeleteQuery(menuInfoParser.getMenuInfoable().getIPermissionClass(), LangUtils.asMap("id:not in", ids));
		deleteq.build();
		DataQuery dq = baseEntityManager.createQuery(deleteq.getSql(), deleteq.getParamsValue().asMap());
		int deleteCount = dq.executeUpdate();
		logger.info("delete {} menu", deleteCount);
	}
	
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
