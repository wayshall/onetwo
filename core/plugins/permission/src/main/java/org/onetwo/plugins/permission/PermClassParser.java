package org.onetwo.plugins.permission;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.plugins.permission.entity.PermissionType;
import org.onetwo.plugins.permission.utils.MenuMetaFields;

public class PermClassParser {
	
	public static PermClassParser create(Class<?> permClass){
		return new PermClassParser(permClass);
	}
	
	private final Class<?> permissionClass;
	
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
	
	public Class<?> getParentPermClass(){
		return permissionClass.getDeclaringClass();
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
	
}
