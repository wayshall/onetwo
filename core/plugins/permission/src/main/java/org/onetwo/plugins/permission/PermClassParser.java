package org.onetwo.plugins.permission;

import java.lang.reflect.Field;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.ReflectUtils;

public class PermClassParser {
	
	public static PermClassParser create(Class<?> permClass){
		return new PermClassParser(permClass);
	}
	
	private final Class<?> permissionClass;
	
	private PermClassParser(Class<?> permClass) {
		super();
		this.permissionClass = permClass;
	}
	public <T> T getFieldValue(String fieldName, Class<T> fieldType) throws Exception {
		return getFieldValue(fieldName, fieldType, null);
	}
	public <T> T getFieldValue(String fieldName, Class<T> fieldType, T def) throws Exception {
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
}
