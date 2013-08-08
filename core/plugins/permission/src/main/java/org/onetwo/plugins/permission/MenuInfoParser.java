package org.onetwo.plugins.permission;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.plugins.permission.entity.MenuEntity;
import org.onetwo.plugins.permission.entity.PageElementEntity;
import org.onetwo.plugins.permission.entity.PermissionEntity;

public class MenuInfoParser {
	
	private final Map<String, PermissionEntity> menuNodeMap = new LinkedHashMap<String, PermissionEntity>();
	private MenuEntity rootMenu;

	@Resource
	private MenuInfoable menuInfoable;
	

	public Map<String, PermissionEntity> getMenuNodeMap() {
		return menuNodeMap;
	}
	
	public String getRootMenuCode(){
		return parseCode(menuInfoable.getRootMenuClass());
	}

	public MenuEntity parseTree(){
		Assert.notNull(menuInfoable);
		Class<?> menuInfoClass = menuInfoable.getRootMenuClass();
		
		PermissionEntity perm = null;
		try {
			perm = parseMenuClass(menuInfoClass);
		} catch (Exception e) {
			throw new BaseException("parse tree error: " + e.getMessage(), e);
		}
		if(!MenuEntity.class.isInstance(perm))
			throw new BaseException("root must be a menu node");
		rootMenu = (MenuEntity)perm;
		return rootMenu;
	}

	protected PermissionEntity parseMenuClass(Class<?> menuClass) throws Exception{
		PermissionEntity perm = parsePermission(menuClass);
		if(perm instanceof PageElementEntity)
			return perm;
		MenuEntity menu = (MenuEntity) perm;
		Class<?>[] childClasses = menuClass.getDeclaredClasses();
		for(Class<?> childCls : childClasses){
			PermissionEntity p = parseMenuClass(childCls);
			if(p instanceof PageElementEntity){
				menu.addPageElement((PageElementEntity)p);
			}else{
				menu.addChild((MenuEntity)p);
			}
		}
		return menu;
	}
	
	public PermissionEntity parsePermission(Class<?> permissionClass) throws Exception{
		Field pageElementField = ReflectUtils.findField(permissionClass, "pageElement");
		boolean pageElement = false;
		if(pageElementField!=null){
			pageElement = pageElementField.getBoolean(permissionClass);
		}

		Object nameValue = ReflectUtils.getFieldValue(permissionClass, "name", true);
		String name = nameValue==null?"":nameValue.toString();
		PermissionEntity perm = null;
		if(pageElement){
			PageElementEntity p = new PageElementEntity();
			p.setName(name);
			perm = p;
		}else{
			MenuEntity menu = new MenuEntity();
			menu.setName(name);
			perm = menu;
		}
		String code = parseCode(permissionClass);
		perm.setCode(code);
		this.menuNodeMap.put(perm.getCode(), perm);
		return perm;
	}
	
	public static String parseCode(Class<?> menuClass){
		String code = menuClass.getSimpleName();
		while(menuClass.getDeclaringClass()!=null){
			menuClass = menuClass.getDeclaringClass();
			code = menuClass.getSimpleName() + "_" + code;
		}
		return code;
	}
	
	public PermissionEntity getMenuNode(Class<?> clazz){
		return getMenuNode(parseCode(clazz));
	}
	
	public PermissionEntity getMenuNode(String code){
		return (PermissionEntity)menuNodeMap.get(code);
	}

	public MenuEntity getRootMenu() {
		return rootMenu;
	}

	public void setRootMenu(MenuEntity rootMenu) {
		this.rootMenu = rootMenu;
	}
	
	
}
