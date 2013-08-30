package org.onetwo.plugins.permission;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.plugins.permission.entity.IFunction;
import org.onetwo.plugins.permission.entity.IMenu;
import org.onetwo.plugins.permission.entity.IPermission;
import org.onetwo.plugins.permission.entity.PermissionType;

public class DefaultMenuInfoParser implements MenuInfoParser {
	
	private final Map<String, IPermission> menuNodeMap = new LinkedHashMap<String, IPermission>();
	private IMenu<?, ?> rootMenu;

	@Resource
	private MenuInfoable menuInfoable;
	private int sortStartIndex = 1000;
	

	public Map<String, IPermission> getMenuNodeMap() {
		return menuNodeMap;
	}
	
	public String getRootMenuCode(){
		return parseCode(menuInfoable.getRootMenuClass());
	}

	/* (non-Javadoc)
	 * @see org.onetwo.plugins.permission.MenuInfoParser#parseTree()
	 */
	@Override
	public IMenu<?, ?> parseTree(){
		Assert.notNull(menuInfoable);
		Class<?> menuInfoClass = menuInfoable.getRootMenuClass();
		
		IPermission perm = null;
		try {
			perm = parseMenuClass(menuInfoClass);
			perm.setSort(1);
		} catch (Exception e) {
			throw new BaseException("parse tree error: " + e.getMessage(), e);
		}
		if(!IMenu.class.isInstance(perm))
			throw new BaseException("root must be a menu node");
		rootMenu = (IMenu<?, ?>)perm;
		return rootMenu;
	}

	protected IPermission parseMenuClass(Class<?> menuClass) throws Exception{
		IPermission perm = parsePermission(menuClass);
		if(perm instanceof IFunction)
			return perm;
		IMenu menu = (IMenu) perm;
		Class<?>[] childClasses = menuClass.getDeclaredClasses();
//		Arrays.sort(childClasses);
		for(Class<?> childCls : childClasses){
			IPermission p = parseMenuClass(childCls);
			if(p instanceof IFunction){
				menu.addFunction((IFunction)p);
			}else{
				menu.addChild((IMenu)p);
			}
		}
		return menu;
	}
	
	public IPermission parsePermission(Class<?> permissionClass) throws Exception{
		Field sortField = ReflectUtils.findField(permissionClass, "sort");
		Number sort = null;
		if(sortField!=null){
			Object pvalue = sortField.get(permissionClass);
			if(!Number.class.isInstance(pvalue))
				throw new BaseException("field[sort] of " + permissionClass + " must be Number.");
			sort = (Number) pvalue;
		}else{
			sort = sortStartIndex++;
		}
		
		Field pageElementField = ReflectUtils.findField(permissionClass, "permissionType");
		PermissionType ptype = PermissionType.MENU;
		if(pageElementField!=null){
			Object pvalue = pageElementField.get(permissionClass);
			if(!PermissionType.class.isInstance(pvalue))
				throw new BaseException("field[permissionType] of " + permissionClass + " must be PermissionType.");
			ptype = (PermissionType) pvalue;
		}
		


		Object nameValue = ReflectUtils.getFieldValue(permissionClass, "name", true);
		String name = nameValue==null?"":nameValue.toString();
		IPermission perm = null;
		if(ptype==PermissionType.FUNCTION){
			perm = (IPermission)ReflectUtils.newInstance(this.menuInfoable.getIFunctionClass());
		}else{
			perm = (IPermission)ReflectUtils.newInstance(this.menuInfoable.getIMenuClass());
		}
		perm.setName(name);
		String code = parseCode(permissionClass);
		perm.setCode(code);
		perm.setSort(sort.intValue());
		this.menuNodeMap.put(perm.getCode(), perm);
		return perm;
	}
	
	/* (non-Javadoc)
	 * @see org.onetwo.plugins.permission.MenuInfoParser#parseCode(java.lang.Class)
	 */
	@Override
	public String parseCode(Class<?> menuClass){
		String code = menuClass.getSimpleName();
		while(menuClass.getDeclaringClass()!=null){
			menuClass = menuClass.getDeclaringClass();
			code = menuClass.getSimpleName() + "_" + code;
		}
		return code;
	}
	
	public IPermission getMenuNode(Class<?> clazz){
		return getMenuNode(parseCode(clazz));
	}
	
	public IPermission getMenuNode(String code){
		return (IPermission)menuNodeMap.get(code);
	}

	public IMenu getRootMenu() {
		return rootMenu;
	}

	public void setRootMenu(IMenu rootMenu) {
		this.rootMenu = rootMenu;
	}

	public MenuInfoable getMenuInfoable() {
		return menuInfoable;
	}
	
	
}
