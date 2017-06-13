package org.onetwo.test.utils;

import java.util.List;
import java.util.Map;

import org.onetwo.common.profiling.TimeProfileStack;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.CUtils;

@SuppressWarnings({"rawtypes"})
public class BaseTest {

	public BaseTest setFieldValue(Object obj, String propName, Object value){
		ReflectUtils.setFieldValue(obj, propName, value);
		return this;
	}

	public BaseTest setFieldValue(Object obj, Class fieldType, Object value){
		ReflectUtils.setFieldValue(obj, fieldType, value);
		return this;
	}

	public static void setStringDefaultValue(Object inst, String val){
		ReflectUtils.setFieldsDefaultValue(inst, String.class, val);
	}

	public static void setFieldsDefaultValue(Object inst, Object...objects){
		Map properties = CUtils.asMap(objects);
		ReflectUtils.setFieldsDefaultValue(inst, properties);
	}
	
	public <T> List<T> times(String method, int count){
		return times(method, count, false);
	}
	
	public <T> List<T> times(String method, int count, boolean printTime){
		if(printTime){
			TimeProfileStack.push(method);
		}
		List<T> result = ReflectUtils.times(this, method, count, false);
		if(printTime){
			TimeProfileStack.pop(method);
		}
		return result;
	}
	/*
	public <T> List<T> newInstance(Class<T> clazz, int count, ListFun<T> closure){
		return ReflectUtils.newInstance(clazz, count, closure);
	}*/
	
}
