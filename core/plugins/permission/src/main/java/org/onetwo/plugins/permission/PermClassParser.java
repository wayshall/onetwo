package org.onetwo.plugins.permission;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.plugins.permission.anno.DelegateMenu;
import org.onetwo.plugins.permission.anno.DelegatedBy;
import org.onetwo.plugins.permission.anno.MenuMapping;
import org.onetwo.plugins.permission.entity.PermissionType;
import org.onetwo.plugins.permission.utils.MenuMetaFields;

public class PermClassParser {
//	private static final String CODE_SEPRATOR = "_";
//	public static final Class<?> ROOT_MENU_TAG = MenuInfoParser.class;
	
	public static PermClassParser create(Class<?> permClass){
		return new PermClassParser(permClass);
	}
	
	private final Class<?> permissionClass;
	private Class<?> parentPermissionClass;
	
	private PermClassParser(Class<?> permClass) {
		super();
		this.permissionClass = permClass;
	}
	
	public Class<?> getPermissionClass() {
		return permissionClass;
	}

	public String getName(){
		Object nameValue = ReflectUtils.getFieldValue(permissionClass, MenuMetaFields.NAME, true);
		String name = nameValue==null?"":nameValue.toString();
		return name;
	}
	
	public String getAppCode(){
		return getFieldValue(MenuMetaFields.APP_CODE, String.class, permissionClass.getSimpleName());
	}
	
	public String generatedSimpleCode(){
		return permissionClass.getSimpleName();
	}
	
	public MenuMapping getMenuMapping(){
		return this.permissionClass.getAnnotation(MenuMapping.class);
	}
	
	public DelegatedBy getDelegatedBy(){
		return this.permissionClass.getAnnotation(DelegatedBy.class);
	}
	
	public Class<?> getDelegatePermClass(){
//		if(!isDelegatedMenu())
//			throw new BaseException("it's not a delegate menu:" + permissionClass);
		Class<?> delegatePermClass = this.getDelegatedBy().value();
		if(delegatePermClass==null)
			throw new BaseException("no delete menu class found:" + permissionClass);
		return delegatePermClass;
	}
	
	public boolean isDeprecated(){
		return permissionClass.getAnnotation(Deprecated.class)!=null;
	}
	
	public boolean isDelegatedMenu(){
		return getDelegatedBy()!=null;
	}
	
	public Class<?>[] getChildrenClasses(){
		return isDelegatedMenu()?getDelegatePermClass().getDeclaredClasses():permissionClass.getDeclaredClasses();
	}
	
	public Class<?> getParentPermissionClass(){
		return parentPermissionClass!=null?parentPermissionClass:permissionClass.getDeclaringClass();
	}
	
	public Number getSort(){
		return getFieldValue(MenuMetaFields.SORT, Number.class);
	}
	
	public PermissionType getPermissionType(){
		return getFieldValue(MenuMetaFields.PERMISSION_TYPE, PermissionType.class, PermissionType.MENU);
	}
	
	public Boolean isHidden(){
		return getFieldValue(MenuMetaFields.HIDDEN, Boolean.class, false);
	}
	
	public Map<?, ?> getParams(){
		return getFieldValue(MenuMetaFields.PARAMS, Map.class, Collections.EMPTY_MAP);
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

	public void setParentPermissionClass(Class<?> parentPermissionClass) {
		this.parentPermissionClass = parentPermissionClass;
	}
	
}
