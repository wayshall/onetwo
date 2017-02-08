package org.onetwo.test.utils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.CUtils;

@SuppressWarnings("rawtypes")
public abstract class TestUtils {


	public static <T> List<T> times(int count, Object delegate, String method, Object... args) {
		return timesWithCount(count, delegate, method, false, args);
	}
	
	public static <T> List<T> timesWithCount(int count, Object delegate, String method, boolean passCountByParams, Object... args) {
		return ReflectUtils.times(delegate, method, count, passCountByParams, args);
	}
	
	public static <T> T create(Class<T> objClass){
		return ReflectUtils.newInstance(objClass);
	}
	
	public static void autoFindAndInjectFields(Object inst, Object...objects){
		Map properties = CUtils.asMap(objects);
		autoFindAndInjectFields(inst, properties);
	}
	
	public static void autoFindAndInjectFields(Object inst, Map properties){
		Collection<Field> fields = ReflectUtils.findFieldsFilterStatic(inst.getClass());
		if(fields==null || fields.isEmpty())
			return ;
//		Map properties = M.c(objects);
		for(Field f :fields){
			if(properties.containsKey(f.getName())){
				ReflectUtils.setBeanFieldValue(f, inst, properties.get(f.getName()));
				continue;
			}
			if(properties.containsKey(f.getType())){
				ReflectUtils.setBeanFieldValue(f, inst, properties.get(f.getType()));
				continue;
			}
		}
	}
	

	public static void setStringDefaultValue(Object inst, String val){
		ReflectUtils.setStringDefaultValue(inst, val);
	}

	public static void setFieldsDefaultValue(Object inst, Object...objects){
		ReflectUtils.setFieldsDefaultValue(inst, objects);
	}
	
	public static void setFieldsDefaultValue(Object inst, Map properties){
		ReflectUtils.setFieldsDefaultValue(inst, properties);
	}

}
