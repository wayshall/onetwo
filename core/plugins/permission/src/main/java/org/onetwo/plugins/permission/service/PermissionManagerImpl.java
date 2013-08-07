package org.onetwo.plugins.permission.service;

import java.util.Map;

import javax.annotation.Resource;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.Assert;
import org.onetwo.plugins.permission.MenuInfoParser;
import org.onetwo.plugins.permission.MenuInfoable;
import org.onetwo.plugins.permission.entity.MenuEntity;
import org.onetwo.plugins.permission.entity.PermissionEntity;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

public class PermissionManagerImpl {
	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());

	private Map<String, PermissionEntity> menuNodeMap;
	private MenuEntity rootMenu;

	@Resource
	private MenuInfoable menuInfoable;
	
	@Resource
	private BaseEntityManager baseEntityManager;
	
	public PermissionManagerImpl() {
	}
	
	public void build(){
		Assert.notNull(menuInfoable);
		Class<?> menuInfoClass = menuInfoable.getRootMenuClass();
		MenuInfoParser menuInfoParser = new MenuInfoParser(menuInfoClass);
		menuInfoParser.parseTree();
		logger.info("menu:\n" + rootMenu);
		this.menuNodeMap = menuInfoParser.getMenuNodeMap();
	}
	
	public PermissionEntity getMenuNode(Class<?> clazz){
		return getMenuNode(MenuInfoParser.parseCode(clazz));
	}
	
	public PermissionEntity getMenuNode(String code){
		return (PermissionEntity)menuNodeMap.get(code);
	}

	@Transactional
	public MenuEntity getRootMenu() {
		if(rootMenu==null){
			Class<?> menuInfoClass = menuInfoable.getRootMenuClass();
			String rootCode = MenuInfoParser.parseCode(menuInfoClass);
			rootMenu = baseEntityManager.findById(MenuEntity.class, rootCode);
		}
		return rootMenu;
	}
	
	@Transactional
	public void syncMenuToDatabase(){
		for(PermissionEntity perm : menuNodeMap.values()){
			logger.info("sync perm[{}]...", perm.getCode());
			baseEntityManager.save(perm);
		}
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
