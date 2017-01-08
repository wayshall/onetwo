package org.onetwo.common.reflect;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
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
	
	static public class DefaultPropertyAcceptor implements BiFunction<PropertyDescriptor, Object, Boolean> {

		@Override
		public Boolean apply(PropertyDescriptor prop, Object val) {
			String clsName = prop.getPropertyType().getName();
			if(clsName.startsWith("groovy.lang.MetaClass") ){
				return false;
			}
			return val!=null;
		}
		
	}

	private String listOpener = "[";
	private String listCloser = "]";
	private String propertyAccesor = ".";
	private String prefix = "";
	private BiFunction<PropertyDescriptor, Object, Boolean> propertyAcceptor = new DefaultPropertyAcceptor();
	private Function<Object, Object> valueConvertor;
	private Function<Object, Boolean> flatableObject;
	private Set<Class<?>> valueTypes = new HashSet<Class<?>>(LangUtils.getSimpleClass());
	private boolean freezed;

	public void freeze(){
		this.checkFreezed();
		this.freezed = true;
	}
	public void checkFreezed(){
		if(this.freezed){
			throw new UnsupportedOperationException("object has freezed!");
		}
	}
	
	public void setPropertyAccesor(String propertyAccesor) {
		this.checkFreezed();
		this.propertyAccesor = propertyAccesor;
	}
	public void setPrefix(String prefix) {
		this.checkFreezed();
		this.prefix = prefix;
	}
	public void setPropertyAcceptor(
			BiFunction<PropertyDescriptor, Object, Boolean> propertyAcceptor) {
		this.checkFreezed();
		this.propertyAcceptor = propertyAcceptor;
	}
	public void setValueConvertor(Function<Object, Object> valueConvertor) {
		this.checkFreezed();
		this.valueConvertor = valueConvertor;
	}
	public void setFlatableObject(Function<Object, Boolean> flatableObject) {
		this.checkFreezed();
		this.flatableObject = flatableObject;
	}
	/***
	 * 简单反射对象的propertyName为key， propertyValue为value
	 * 默认忽略value==null的属性，参见DefaultPropertyAcceptor
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
	 */
	public Map<String, Object> toFlatMap(final Object obj){
		final Map<String, Object> params = new HashMap<>();
		toFlatMap(params, obj);
		return params;
	}
	
	public BeanToMapConvertor addValueType(Class<?> clazz){
		this.checkFreezed();
		this.valueTypes.add(clazz);
		return this;
	}
	

	public boolean isMappableValue(Object value){
		if(value==null)
			return true;
		return valueTypes.contains(value.getClass()) || (flatableObject!=null && !flatableObject.apply(value));
//		return valueTypes.contains(value.getClass());
	}

	/****
	 * 
	 * 简单反射对象的propertyName为key， propertyValue为value
	 * 默认忽略value==null的属性，参见DefaultPropertyAcceptor
	 * @param params
	 * @param obj
	 */
	public void toFlatMap(final Map<String, Object> params, final Object obj){
		flatObject(prefix, obj, (k, v, c)->params.put(k, v));
	}
	
	/****
	 * 
	 * 简单反射对象的propertyName为key， propertyValue为value
	 * 默认忽略value==null的属性，参见DefaultPropertyAcceptor
	 * @param prefixName
	 * @param obj
	 * @param valuePutter
	 */
	public <T> void flatObject(final String prefixName, final Object obj, ValuePutter valuePutter){
		flatObject(prefixName, obj, valuePutter, null);
	}
	@SuppressWarnings("unchecked")
	private <T> void flatObject(final String prefixName, final Object obj, ValuePutter valuePutter, PropertyContext keyContext){
		if(isMappableValue(obj)){
			valuePutter.put(prefixName, obj, keyContext);
		}else if(Map.class.isInstance(obj)){
			String mapPrefixName = prefixName;
			if(StringUtils.isNotBlank(prefixName)){
				mapPrefixName = prefixName+this.propertyAccesor;
			}
			for(Entry<String, Object> entry : ((Map<String, Object>)obj).entrySet()){
				if(isMappableValue(entry.getValue())){
					valuePutter.put(mapPrefixName+entry.getKey(), entry.getValue(), null);
				}else{
					flatObject(mapPrefixName+entry.getKey(), entry.getValue(), valuePutter);
				}
			}
		}else if(LangUtils.isMultiple(obj)){
			List<Object> list = LangUtils.asList(obj);
			int index = 0;
			for(Object o : list){
				String listPrefixName = prefixName + this.listOpener+index+this.listCloser;
				if(isMappableValue(o)){
					valuePutter.put(listPrefixName, o, null);
				}else{
					flatObject(listPrefixName, o, valuePutter);
				}
				index++;
			}
		}else{
			/*if(flatableObject==null || !flatableObject.apply(obj)){
				valuePutter.put(prefixName, obj);
				return ;
			}*/
			ReflectUtils.listProperties(obj.getClass(), prop-> {
				Object val = ReflectUtils.getProperty(obj, prop);
				if (propertyAcceptor==null || propertyAcceptor.apply(prop, val)){
					if(valueConvertor!=null){
						val = valueConvertor.apply(val);
					}
					PropertyContext propContext = new PropertyContext(obj, prop, prop.getName());
					if(StringUtils.isBlank(prefixName)){
						flatObject(prop.getName(), val, valuePutter, propContext);
					}else{
						flatObject(prefixName+propertyAccesor+prop.getName(), val, valuePutter, propContext);
					}
				}
			});
		}
	}

	public static interface ValuePutter {
		void put(String key, Object value, PropertyContext keyContext);
	}
	
	public static class PropertyContext {
		final private Object source;
		final private PropertyDescriptor property;
		final private String name;
		public PropertyContext(Object source, PropertyDescriptor property,
				String originName) {
			super();
			this.source = source;
			this.property = property;
			this.name = originName;
		}
		public Object getSource() {
			return source;
		}
		public PropertyDescriptor getProperty() {
			return property;
		}
		public Field getField(){
			return ClassIntroManager.getInstance().getIntro(source.getClass()).getField(name);
		}
		public String getName() {
			return name;
		}
	}

	public static class BeanToMapBuilder {
		public static BeanToMapBuilder newBuilder(){
			return new BeanToMapBuilder();
		}
		private BeanToMapConvertor beanToFlatMap = new BeanToMapConvertor();


		public BeanToMapBuilder propertyAcceptor(BiFunction<PropertyDescriptor, Object, Boolean> propertyAcceptor) {
			beanToFlatMap.setPropertyAcceptor(propertyAcceptor);
			return this;
		}

		public BeanToMapBuilder flatableObject(Function<Object, Boolean> flatableObject) {
			beanToFlatMap.setFlatableObject(flatableObject);
			return this;
		}

		public BeanToMapBuilder valueConvertor(Function<Object, Object> valueConvertor) {
			beanToFlatMap.setValueConvertor(valueConvertor);
			return this;
		}

		public BeanToMapBuilder prefix(String prefix) {
			beanToFlatMap.setPrefix(prefix);
			return this;
		}
		
		/*public Map<String, Object> toMap(Object obj){
			return beanToFlatMap.toMap(obj);
		}
		
		public Map<String, Object> toFlatMap(Object obj){
			return beanToFlatMap.toFlatMap(obj);
		}*/
		public BeanToMapConvertor build(){
			this.beanToFlatMap.freeze();
			return beanToFlatMap;
		}
	}
	
	
}
