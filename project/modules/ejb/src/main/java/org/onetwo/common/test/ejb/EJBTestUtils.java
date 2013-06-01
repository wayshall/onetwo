package org.onetwo.common.test.ejb;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import javax.ejb.EJB;

import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.map.M;

@SuppressWarnings("rawtypes")
public abstract class EJBTestUtils {

	public static <T> T create(Class<T> objClass){
		return ReflectUtils.newInstance(objClass);
	}
	
	public static void autoFindAndInjectFields(Object inst, Object...objects){
		Map properties = M.c(objects);
		autoFindAndInjectFields(inst, properties);
	}
	
	public static void autoFindAndInjectFields(Object inst, Map properties){
		Collection<Field> fields = ReflectUtils.findFieldsFilterStatic(inst.getClass());
		if(fields==null || fields.isEmpty())
			return ;
//		Map properties = M.c(objects);
		for(Field f :fields){
			if(properties.containsKey(f.getName())){
				ReflectUtils.setFieldValue(f, inst, properties.get(f.getName()));
				continue;
			}
			if(properties.containsKey(f.getType())){
				ReflectUtils.setFieldValue(f, inst, properties.get(f.getType()));
				continue;
			}
			EJB ejbAnno = f.getAnnotation(EJB.class);
			if(ejbAnno==null){
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
