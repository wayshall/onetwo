package org.onetwo.common.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.onetwo.common.utils.list.JFishList;

public class ClassWrapper<T> {
	
	public static <T> ClassWrapper<T> wrap(Class<T> clazz){
		ClassWrapper<T> intro = new ClassWrapper<T>(clazz);
		return intro;
	}

	
	private final Class<T> clazz;

	public ClassWrapper(Class<T> clazz) {
		super();
		this.clazz = clazz;
	}

	public Class<?> getClazz() {
		return clazz;
	}


	public Collection<PropertyDescriptor> getProperties(){
		return JFishList.wrap(ReflectUtils.desribProperties(clazz));
	}

	public PropertyDescriptor getProperty(String propName){
		PropertyDescriptor prop = ReflectUtils.getPropertyDescriptor(clazz, propName);
		return prop;
	}
	
	public JFishProperty getJFishProperty(String propName, boolean isField){
		if(isField){
			Field field = getField(propName);
			return new JFishFieldInfoImpl(clazz, field);
		}else{
			PropertyDescriptor pd = getProperty(propName);
			return new JFishPropertyInfoImpl(clazz, pd);
		}
	}
	
	/*public JFishProperty getJFishProperty(String propName){
		this._loadProperties();
		PropertyDescriptor prop = propCaches.get(propName);
		return new JFishPropertyInfoImpl(this, prop);
	}*/
	
	public Collection<Field> getFields(){
		return ReflectUtils.findAllFields(clazz);
	}
	
	public Field getField(String fieldName){
		return ReflectUtils.findField(clazz, fieldName);
	}

	public T newInstance(){
		T bean = (T)ReflectUtils.newInstance(clazz);
		return bean;
	}
	
	public T newInstance(Map<String, ?> propValues, TypeJudge typeJudge){
		T bean = newInstance();
		Collection<PropertyDescriptor> props = getProperties();
		Object val = null;
		for(PropertyDescriptor prop : props){
			val = propValues.get(prop.getName());
			if(typeJudge!=null){
				val = LangUtils.judgeType(val, prop.getPropertyType(), typeJudge);
			}
			ReflectUtils.setProperty(bean, prop, val);
		}
		return bean;
	}
	
	/********
	 * 遍历map
	 * @param propValues
	 * @return
	 */
	public T newBy(Map<String, ?> propValues){
		T bean = newInstance();
		for(Entry<String, ?> entry : propValues.entrySet()){
			ReflectUtils.setExpr(bean, entry.getKey(), entry.getValue());
		}
		return bean;
	}
	
	/***************
	 * 遍历字段和属性
	 * @param propValues
	 * @return
	 */
	public T newFrom(Map<String, ?> propValues){
		T bean = newInstance();

		Collection<String> fieldNames = ReflectUtils.findInstanceFieldNames(clazz, Set.class);
		Collection<String> propNames = ReflectUtils.desribPropertiesName(clazz, Set.class);
		propNames.addAll(fieldNames);
		Object val = null;
		for(String name : propNames){
			val = propValues.get(name);
			if(val!=null)
				ReflectUtils.setExpr(bean, name, val);
		}
		
		return bean;
	}

	public boolean isCollectionType(){
		return Collection.class.isAssignableFrom(clazz);
	}

	public boolean isMapType(){
		return Map.class.isAssignableFrom(clazz);
	}
	
	public String toString(){
		return "class wraper["+clazz+"]";
	}
}
