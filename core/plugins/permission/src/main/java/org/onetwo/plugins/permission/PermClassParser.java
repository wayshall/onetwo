package org.onetwo.plugins.permission;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.plugins.permission.anno.MenuMapping;
import org.onetwo.plugins.permission.anno.ProxyMenu;
import org.onetwo.plugins.permission.entity.PermissionType;

public class PermClassParser {
	public static final String APP_CODE = "appCode";
	public static final String SORT = "sort";
	public static final String PERMISSION_TYPE = "permissionType";
	public static final String HIDDEN = "hidden";
	public static final String PARAMS = "params";
	public static final String NAME = "name";
	public static final String CHILDREN = "children";
	
//	private static final String CODE_SEPRATOR = "_";
//	public static final Class<?> ROOT_MENU_TAG = MenuInfoParser.class;
	
	public static PermClassParser create(Class<?> permClass, Class<?> parentPermissionClass){
		return new PermClassParser(permClass, parentPermissionClass);
	}
	
	private final Class<?> permissionClass;
	private final Class<?> parentPermissionClass;
	
	private PermClassParser(Class<?> permClass, Class<?> parentPermissionClass) {
		super();
		this.permissionClass = permClass;
		this.parentPermissionClass = parentPermissionClass;
	}
	
	public Class<?> getPermissionClass() {
		return permissionClass;
	}
	
	public Class<?> getActualPermissionClass() {
		return isProxyMenu()?getProxyPermClass():permissionClass;
	}

	public String getName(){
		Object nameValue = ReflectUtils.getFieldValue(getActualPermissionClass(), NAME, true);
		String name = nameValue==null?"":nameValue.toString();
		return name;
	}
	
	public String getAppCode(){
		return getFieldValue(APP_CODE, String.class, permissionClass.getSimpleName());
	}
	
	public String generatedSimpleCode(){
		return permissionClass.getSimpleName();
	}
	
	public Class<?> getMappingParentClass(){
		MenuMapping map = this.permissionClass.getAnnotation(MenuMapping.class);
		return map==null?null:map.parent();
	}
	
	public ProxyMenu getProxyMenu(){
		return this.permissionClass.getAnnotation(ProxyMenu.class);
	}
	
	public Class<?> getProxyPermClass(){
//		if(!isDelegatedMenu())
//			throw new BaseException("it's not a delegate menu:" + permissionClass);
		Class<?> delegatePermClass = this.getProxyMenu().value();
		if(delegatePermClass==null)
			throw new BaseException("no delete menu class found:" + permissionClass);
		return delegatePermClass;
	}
	
	public boolean isDeprecated(){
		return permissionClass.getAnnotation(Deprecated.class)!=null;
	}
	
	public boolean isProxyMenu(){
		return getProxyMenu()!=null;
	}
	
	public Class<?>[] getChildrenClasses(){
		JFishList<Class<?>> list = JFishList.create();
		Class<?>[] children =  isProxyMenu()?getProxyPermClass().getDeclaredClasses():permissionClass.getDeclaredClasses();
		list.addArray(children).addArray(getChildren());
		return list.toArray(new Class<?>[0]);
	}
	
	public Class<?> getParentPermissionClass(){
		return parentPermissionClass!=null?parentPermissionClass:permissionClass.getDeclaringClass();
	}
	
	protected Class<?>[] getChildren(){
		return getFieldValue(CHILDREN, Class[].class);
	}
	
	public Number getSort(){
		return getFieldValue(SORT, Number.class);
	}
	
	public PermissionType getPermissionType(){
		return getFieldValue(PERMISSION_TYPE, PermissionType.class, PermissionType.MENU);
	}
	
	public Boolean isHidden(){
		return getFieldValue(HIDDEN, Boolean.class, false);
	}
	
	public Map<?, ?> getParams(){
		return getFieldValue(PARAMS, Map.class, Collections.EMPTY_MAP);
	}
	
	private <T> T getFieldValue(String fieldName, Class<T> fieldType) {
		return getFieldValue(fieldName, fieldType, null);
	}
	
	private <T> T getFieldValue(String fieldName, Class<T> fieldType, T def) {
		Field pageElementField = ReflectUtils.findField(permissionClass, fieldName);
		T fieldValue = def;
		if(pageElementField!=null){
			Object pvalue;
			try {
				pvalue = pageElementField.get(permissionClass);
			} catch (Exception e) {
				throw new BaseException("get field error: " + e.getMessage());
			} 
			if(!fieldType.isInstance(pvalue))
				throw new BaseException("field["+fieldName+"] of " + permissionClass + " must be " + fieldType);
			fieldValue = (T) pvalue;
		}
		return fieldValue;
	}

/*	public void setParentPermissionClass(Class<?> parentPermissionClass) {
		this.parentPermissionClass = parentPermissionClass;
	}*/
	
}
