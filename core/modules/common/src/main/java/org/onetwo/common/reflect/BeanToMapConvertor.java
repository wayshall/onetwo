package org.onetwo.common.reflect;

import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

public class BeanToMapConvertor {

	private String listOpener = "[";
	private String listCloser = "]";
	private String propertyAccesor = ".";
	private String prefix = "";
	private BiFunction<PropertyDescriptor, Object, Boolean> propertyAcceptor = (prop, val)->val!=null;
	private Function<Object, Object> valueConvertor;
	private Set<Class<?>> valueTypes = new HashSet<Class<?>>(LangUtils.getSimpleClass());
	
	/***
	 * 简单反射对象的propertyName为key， propertyValue为value
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> toMap(final Object obj){
//		return ReflectUtils.toMap(obj, propertyAcceptor, valueConvertor);
		if (obj == null)
			return Collections.emptyMap();
		
		if(obj.getClass().isArray())
			return CUtils.asMap((Object[])obj);
		
		if(obj instanceof Map)
			return (Map<String, Object>)obj;
		
		PropertyDescriptor[] props = ReflectUtils.desribProperties(obj.getClass());
		if (props == null || props.length == 0)
			return Collections.emptyMap();
		Map<String, Object> rsMap = new HashMap<>();
		Object val = null;
		for (PropertyDescriptor prop : props) {
			val = ReflectUtils.getProperty(obj, prop);
			if (propertyAcceptor==null || propertyAcceptor.apply(prop, val)){
				if(valueConvertor!=null)
					val = valueConvertor.apply(val);
				rsMap.put(prop.getName(), val);
			}
		}
		return rsMap;
	}
	
	/***
	 * 递归解释嵌套对象
	 * @param obj
	 * @return
	 */
	public Map<String, Object> toFlatMap(final Object obj){
		final Map<String, Object> params = new HashMap<>();
		toFlatMap(params, obj);
		return params;
	}
	
	public BeanToMapConvertor addValueType(Class<?> clazz){
		this.valueTypes.add(clazz);
		return this;
	}
	

	public boolean isValueType(Class<?> clazz){
		return valueTypes.contains(clazz);
	}

	public void toFlatMap(final Map<String, Object> params, final Object obj){
		flatObject(prefix, obj, (k, v)->params.put(k, v));
	}
	
	public <T> void flatObject(final String prefixName, final Object obj, ValuePutter valuePutter){
		if(obj==null)
			return ;
		if(isValueType(obj.getClass())){
			valuePutter.put(prefixName, obj==null?"":obj.toString());
		}else if(Map.class.isInstance(obj)){
			String mapPrefixName = prefixName;
			if(StringUtils.isNotBlank(prefixName)){
				mapPrefixName = prefixName+this.propertyAccesor;
			}
			for(Entry<String, Object> entry : ((Map<String, Object>)obj).entrySet()){
				if(entry.getValue()==null){
					valuePutter.put(mapPrefixName+entry.getKey(), "");
				}else if(String.class.isInstance(entry.getValue()) || isValueType(entry.getValue().getClass())){
					valuePutter.put(mapPrefixName+entry.getKey(), entry.getValue()==null?"":entry.getValue().toString());
				}else{
					flatObject(mapPrefixName+entry.getKey(), entry.getValue(), valuePutter);
				}
			}
		}else if(LangUtils.isMultiple(obj)){
			List<Object> list = LangUtils.asList(obj);
			int index = 0;
			for(Object o : list){
				String listPrefixName = prefixName + this.listOpener+index+this.listCloser;
				if(String.class.isInstance(o) || LangUtils.isBaseTypeObject(o)){
					valuePutter.put(listPrefixName, o==null?"":o.toString());
				}else{
					flatObject(listPrefixName, o, valuePutter);
				}
				index++;
			}
		}else{
			ReflectUtils.listProperties(obj.getClass(), prop-> {
				Object val = ReflectUtils.getProperty(obj, prop);
				if (propertyAcceptor==null || propertyAcceptor.apply(prop, val)){
					if(valueConvertor!=null){
						val = valueConvertor.apply(val);
					}
					if(StringUtils.isBlank(prefixName)){
						flatObject(prop.getName(), val, valuePutter);
					}else{
						flatObject(prefixName+propertyAccesor+prop.getName(), val, valuePutter);
					}
				}
			});
		}
	}

	public static interface ValuePutter {
		void put(String key, Object value);
	}

	public static class BeanToMapBuilder {
		public static BeanToMapBuilder newBuilder(){
			return new BeanToMapBuilder();
		}
		private BeanToMapConvertor beanToFlatMap = new BeanToMapConvertor();
		

		public BeanToMapBuilder propertyAcceptor(BiFunction<PropertyDescriptor, Object, Boolean> propertyAcceptor) {
			beanToFlatMap.propertyAcceptor = propertyAcceptor;
			return this;
		}

		public BeanToMapBuilder valueConvertor(Function<Object, Object> valueConvertor) {
			beanToFlatMap.valueConvertor = valueConvertor;
			return this;
		}

		public BeanToMapBuilder prefix(String prefix) {
			beanToFlatMap.prefix = prefix;
			return this;
		}
		
		public Map<String, Object> toMap(Object obj){
			return beanToFlatMap.toMap(obj);
		}
		
		public Map<String, Object> toFlatMap(Object obj){
			return beanToFlatMap.toFlatMap(obj);
		}
	}
	
	
}
