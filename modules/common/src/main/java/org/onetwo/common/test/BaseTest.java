package org.onetwo.common.test;

import java.util.List;
import java.util.Map;

import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.map.M;

@SuppressWarnings({"rawtypes"})
public class BaseTest {

	public BaseTest injectField(Object obj, String propName, Object value){
		ReflectUtils.setBean(obj, propName, value);
		return this;
	}

	public BaseTest injectField(Object obj, Class fieldType, Object value){
		ReflectUtils.setBean(obj, fieldType, value);
		return this;
	}

	public static void setStringDefaultValue(Object inst, String val){
		ReflectUtils.setFieldsDefaultValue(inst, String.class, val);
	}

	public static void setFieldsDefaultValue(Object inst, Object...objects){
		Map properties = M.c(objects);
		ReflectUtils.setFieldsDefaultValue(inst, properties);
	}
	
	public <T> List<T> times(String method, int count){
		return times(method, count, false);
	}
	
	public <T> List<T> times(String method, int count, boolean printTime){
		boolean old = UtilTimerStack.isActive();
		if(printTime){
			UtilTimerStack.setActive(printTime);
			UtilTimerStack.push(method);
			UtilTimerStack.setActive(old);
		}
		List<T> result = ReflectUtils.times(this, method, count, false);
		if(printTime){
			UtilTimerStack.setActive(printTime);
			UtilTimerStack.pop(method);
			UtilTimerStack.setActive(old);
		}
		return result;
	}
	/*
	public <T> List<T> newInstance(Class<T> clazz, int count, ListFun<T> closure){
		return ReflectUtils.newInstance(clazz, count, closure);
	}*/
	
}
