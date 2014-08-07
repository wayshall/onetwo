package org.onetwo.plugins.permission;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.utils.JFishResourcesScanner;
import org.onetwo.common.spring.utils.ResourcesScanner;
import org.onetwo.common.spring.utils.ScanResourcesCallback;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.map.CasualMap;
import org.onetwo.plugins.permission.anno.MenuMapping;
import org.onetwo.plugins.permission.entity.IFunction;
import org.onetwo.plugins.permission.entity.IMenu;
import org.onetwo.plugins.permission.entity.IPermission;
import org.onetwo.plugins.permission.entity.PermissionType;
import org.onetwo.plugins.permission.utils.MenuMetaFields;
import org.springframework.core.type.classreading.MetadataReader;

public class DefaultMenuInfoParser implements MenuInfoParser {
	private static final String CODE_SEPRATOR = "_";

	private final Map<String, IPermission> permissionMap = new LinkedHashMap<String, IPermission>(50);
	private final Map<Class<?>, IPermission> permissionMapByClass = new LinkedHashMap<Class<?>, IPermission>(50);
	private IMenu<? extends IMenu<?, ?> , ? extends IFunction<?>> rootMenu;
	private final ResourcesScanner scaner = new JFishResourcesScanner();

	@Resource
	private PermissionConfig menuInfoable;
	private int sortStartIndex = 1000;
	

	public Map<String, ? extends IPermission> getPermissionMap() {
		return permissionMap;
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

		String appCode = null;
		IPermission perm = null;
		try {
			appCode = getFieldValue(menuInfoClass, MenuMetaFields.APP_CODE, String.class, menuInfoClass.getSimpleName());
			/*if(StringUtils.isBlank(sysname)){
				throw new BaseException("RootMenuClass must has a sysname field and it's value can not be blank.");
			}*/
			perm = parseMenuClass(menuInfoClass, appCode);
			perm.setSort(1);
		} catch (Exception e) {
			throw new BaseException("parse tree error: " + e.getMessage(), e);
		}
		if(!IMenu.class.isInstance(perm))
			throw new BaseException("root must be a menu node");
		rootMenu = (IMenu<?, ?>)perm;
		
		String[] childMenuPackages = menuInfoable.getChildMenuPackages();
		if(LangUtils.isEmpty(childMenuPackages))
			return rootMenu;
		
		List<Class<?>> childMenuClass = scaner.scan(new ScanResourcesCallback<Class<?>>(){

			/*@Override
			public boolean isCandidate(MetadataReader metadataReader) {
				if (metadataReader.getAnnotationMetadata().hasAnnotation(MenuMapping.class.getName()))
					return true;
				return false;
			}*/

			@Override
			public Class<?> doWithCandidate(MetadataReader metadataReader, org.springframework.core.io.Resource resource, int count) {
				if (!metadataReader.getAnnotationMetadata().hasAnnotation(MenuMapping.class.getName()))
					return null;
				Class<?> cls = ReflectUtils.loadClass(metadataReader.getClassMetadata().getClassName());
				return cls;
			}
			
		}, childMenuPackages);
		
		if(LangUtils.isEmpty(childMenuClass))
			return rootMenu;
		
		for(Class<?> childMc : childMenuClass){
			IPermission childPerm =  parseMenuClass(childMc, appCode);
			if(IMenu.class.isInstance(childPerm)){
				rootMenu.addChild((IMenu)childPerm);
			}else{
				rootMenu.addFunction((IFunction)childPerm);
			}
		}
		
		return rootMenu;
	}
	

	protected <T extends IPermission> T parseMenuClass(Class<?> menuClass, String syscode){
		IPermission perm;
		try {
			if(menuClass.getAnnotation(Deprecated.class)!=null)
				return null;
			perm = parsePermission(menuClass, syscode);
		} catch (Exception e) {
			throw new BaseException("parser permission error: " + e.getMessage(), e);
		}
		if(perm instanceof IFunction)
			return (T)perm;
		
		IMenu<? extends IMenu<?, ?> , ? extends IFunction<?>> menu = (IMenu) perm;
		Class<?>[] childClasses = menuClass.getDeclaredClasses();
//		Arrays.sort(childClasses);
		for(Class<?> childCls : childClasses){
			IPermission p = parseMenuClass(childCls, syscode);
			if(p==null)
				continue;
			if(p instanceof IFunction){
				menu.addFunction((IFunction)p);
			}else{
				menu.addChild((IMenu)p);
			}
		}
		return (T)menu;
	}
	

	private <T> T getFieldValue(Class<?> permissionClass, String fieldName, Class<T> fieldType) throws Exception {
		return getFieldValue(permissionClass, fieldName, fieldType, null);
	}
	private <T> T getFieldValue(Class<?> permissionClass, String fieldName, Class<T> fieldType, T def) throws Exception {
		Field pageElementField = ReflectUtils.findField(permissionClass, fieldName);
		T fieldValue = def;
		if(pageElementField!=null){
			Object pvalue = pageElementField.get(permissionClass);
			if(!fieldType.isInstance(pvalue))
				throw new BaseException("field["+fieldName+"] of " + permissionClass + " must be " + fieldType);
			fieldValue = (T) pvalue;
		}
		return fieldValue;
	}
	
	public IPermission parsePermission(Class<?> permissionClass, String syscode) throws Exception{
		Number sort = getFieldValue(permissionClass, MenuMetaFields.SORT, Number.class);
		if(sort==null){
			sort = sortStartIndex++;
		}
		/*
		Field sortField = ReflectUtils.findField(permissionClass, "sort");
		if(sortField!=null){
			Object pvalue = sortField.get(permissionClass);
			if(!Number.class.isInstance(pvalue))
				throw new BaseException("field[sort] of " + permissionClass + " must be Number.");
			sort = (Number) pvalue;
		}else{
			sort = sortStartIndex++;
		}*/

		PermissionType ptype = getFieldValue(permissionClass, MenuMetaFields.PERMISSION_TYPE, PermissionType.class, PermissionType.MENU);
		/*
		Field pageElementField = ReflectUtils.findField(permissionClass, "permissionType");
		PermissionType ptype = PermissionType.MENU;
		if(pageElementField!=null){
			Object pvalue = pageElementField.get(permissionClass);
			if(!PermissionType.class.isInstance(pvalue))
				throw new BaseException("field[permissionType] of " + permissionClass + " must be PermissionType.");
			ptype = (PermissionType) pvalue;
		}*/

		Boolean hidden = getFieldValue(permissionClass, MenuMetaFields.HIDDEN, Boolean.class, false);
		/*Boolean hidden = false;
		Field menuHiddenField = ReflectUtils.findField(permissionClass, "hidden");
		if(menuHiddenField!=null){
			Object hiddenValue = menuHiddenField.get(permissionClass);
			if(!Boolean.class.isInstance(hiddenValue))
				throw new BaseException("field[hidden] of " + permissionClass + " must be Boolean.");
			hidden = (Boolean) hiddenValue;
		}*/

		Object nameValue = ReflectUtils.getFieldValue(permissionClass, MenuMetaFields.NAME, true);
		String name = nameValue==null?"":nameValue.toString();
		IPermission perm = null;
		if(ptype==PermissionType.FUNCTION){
			perm = (IPermission)ReflectUtils.newInstance(this.menuInfoable.getIFunctionClass());
		}else{
			IMenu<?, ?> menu = (IMenu<?, ?>)ReflectUtils.newInstance(this.menuInfoable.getIMenuClass());
			Map<?, ?> param = getFieldValue(permissionClass, MenuMetaFields.PARAMS, Map.class, Collections.EMPTY_MAP);
			CasualMap casualmap = new CasualMap().addMapWithFilter(param);
			menu.setUrl(casualmap.toParamString());
			perm = menu;
		}
		perm.setName(name);
		String code = parseCode(permissionClass);
		perm.setCode(code);
		perm.setSort(sort.intValue());
		perm.setHidden(hidden);
		perm.setAppCode(syscode);
		this.permissionMap.put(perm.getCode(), perm);
		this.permissionMapByClass.put(permissionClass, perm);
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
			code = menuClass.getSimpleName() + CODE_SEPRATOR + code;
		}
		MenuMapping mapping = menuClass.getAnnotation(MenuMapping.class);
		if(mapping!=null){
			Class<?> pcls = mapping.parent();
			IPermission perm = this.permissionMapByClass.get(pcls);
			if(perm==null)
				throw new BaseException("parse menu class["+menuClass+"] error. no parent menu found: " + pcls);
			code = perm.getCode() + CODE_SEPRATOR + code;
		}
		return code;
	}
	
	public IPermission getPermission(Class<?> clazz){
		return this.permissionMapByClass.get(clazz);
	}
	
	public IPermission getMenuNode(String code){
		return (IPermission)permissionMap.get(code);
	}

	public IMenu getRootMenu() {
		return rootMenu;
	}

	public void setRootMenu(IMenu rootMenu) {
		this.rootMenu = rootMenu;
	}

	public PermissionConfig getMenuInfoable() {
		return menuInfoable;
	}
	
	
}
