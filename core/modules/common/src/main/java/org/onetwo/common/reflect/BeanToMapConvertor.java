package org.onetwo.common.reflect;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import org.onetwo.common.annotation.IgnoreField;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.FieldName;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

/*****
 * see also spring utils toFlatMap
 * @author way
 *
 */
public class BeanToMapConvertor implements Cloneable {
	private static final String GROOVY_META = "groovy.lang.MetaClass";
	/****
	 * 默认忽略值为null的属性
	 * @author way
	 *
	 */
	static public class DefaultPropertyAcceptor implements BiFunction<PropertyContext, Object, Boolean> {

		@Override
		public Boolean apply(PropertyContext prop, Object val) {
			String clsName = prop.getProperty().getPropertyType().getName();
			if(clsName.startsWith(GROOVY_META) ){
				return false;
			} else {
				Field field = prop.getField();
				if (field!=null && field.isAnnotationPresent(IgnoreField.class)) {
					return false;
				}
				Method readMethod = prop.getProperty().getReadMethod();
				if (readMethod!=null && readMethod.isAnnotationPresent(IgnoreField.class)) {
					return false;
				}
			}
			return val!=null;
		}
		
	}
	
	static public class ExcludePropertyAcceptor extends DefaultPropertyAcceptor implements BiFunction<PropertyContext, Object, Boolean> {
		
		private Collection<String> excludeProperties;
		
		public ExcludePropertyAcceptor(Collection<String> excludeProperties){
			this.excludeProperties = excludeProperties;
		}

		@Override
		public Boolean apply(PropertyContext prop, Object val) {
			if(excludeProperties.contains(prop.getName())){
				return false;
			}
			return super.apply(prop, val);
		}
		
	}

	/***
	 * 不可扁平化的类型，即可直接映射为值的类型
	 */
	@SuppressWarnings("serial")
	private final static Collection<Class<?>> NOT_FLATABLE_VALUE_TYPES = new HashSet<Class<?>>(LangUtils.getSimpleClass()){
		{
//			add(Enum.class);
			add(URL.class);
			add(URI.class);
			add(Class.class);
			add(ClassLoader.class);
		}
	};

	public final static Function<Object, Boolean> DEFAULT_FLATABLE = obj->{
		Class<?> valueType = obj.getClass();
		if(NOT_FLATABLE_VALUE_TYPES.contains(valueType)){
			return false;
		}else if(Enum.class.isAssignableFrom(valueType)){
			//枚举类也不再扁平
			return false;
		}
		return true;
	};
	
	public static interface PropertyNameConvertor {
		String convert(PropertyContext ctx);
	}
	
	public static class DefaultPropertyNameConvertor implements PropertyNameConvertor {
		protected boolean enableFieldNameAnnotation = false;
		protected boolean enableUnderLineStyle = false;
		
		public DefaultPropertyNameConvertor(boolean enableFieldNameAnnotation, boolean enableUnderLineStyle) {
			super();
			this.enableFieldNameAnnotation = enableFieldNameAnnotation;
			this.enableUnderLineStyle = enableUnderLineStyle;
		}

		@Override
		public String convert(PropertyContext ctx) {
			String name = ctx.name;
			if(enableFieldNameAnnotation && ctx.source!=null){
				FieldName fn = ReflectUtils.getFieldNameAnnotation(ctx.source.getClass(), name);
				if(fn!=null){
					name = fn.value();
				}
			}
			if(enableUnderLineStyle){
				name = StringUtils.convert2UnderLineName(name);
			}
			return name;
		}
	}
	
	private String listOpener = "[";
	private String listCloser = "]";
	private String propertyAccesor = ".";
	private String prefix = "";
	private BiFunction<PropertyContext, Object, Boolean> propertyAcceptor;
	private PropertyNameConvertor propertyNameConvertor;
	private BiFunction<PropertyDescriptor, Object, Object> valueConvertor;
	
	private Function<Object, Boolean> flatableObject = DEFAULT_FLATABLE;
//	private Set<Class<?>> valueTypes = new HashSet<Class<?>>(LangUtils.getSimpleClass());
//	private boolean freezed;
//	protected boolean enableFieldNameAnnotation = false;
//	protected boolean enableUnderLineStyle = false;
	
	protected BeanToMapConvertor(){
	}
	
	/*public BeanToMapConvertor addNotFlatableTypes(Class<?>... types){
		mapableValueTypes.addAll(Arrays.asList(types));
		return this;
	}*/

	/*public void freeze(){
		this.checkFreezed();
		this.freezed = true;
	}
	public void checkFreezed(){
		if(this.freezed){
			throw new UnsupportedOperationException("object has freezed!");
		}
	}*/
	
	
	public void setPropertyAccesor(String propertyAccesor) {
//		this.checkFreezed();
		this.propertyAccesor = propertyAccesor;
	}
	
	@Override
	public BeanToMapConvertor clone() /*throws CloneNotSupportedException */{
		BeanToMapConvertor convertor = new BeanToMapConvertor();
		convertor.listOpener = this.listOpener;
		convertor.listCloser = this.listCloser;
		convertor.propertyAccesor = this.propertyAccesor;
		convertor.prefix = this.prefix;
		convertor.propertyAcceptor = this.propertyAcceptor;
		convertor.valueConvertor = this.valueConvertor;
		convertor.flatableObject = this.flatableObject;
		convertor.propertyNameConvertor = this.propertyNameConvertor;
		return convertor;
	}

	public void setPrefix(String prefix) {
//		this.checkFreezed();
		this.prefix = prefix;
	}
	public void setPropertyAcceptor(
			BiFunction<PropertyContext, Object, Boolean> propertyAcceptor) {
//		this.checkFreezed();
		this.propertyAcceptor = propertyAcceptor;
	}
	public void setValueConvertor(BiFunction<PropertyDescriptor, Object, Object> valueConvertor) {
//		this.checkFreezed();
		this.valueConvertor = valueConvertor;
	}
	public void setFlatableObject(Function<Object, Boolean> flatableObject) {
//		this.checkFreezed();
		this.flatableObject = flatableObject;
	}
	
	public void setPropertyNameConvertor(PropertyNameConvertor propertyNameConvertor) {
		this.propertyNameConvertor = propertyNameConvertor;
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
		
		ObjectWrapper objWrapper = objectWrapper(obj);
		PropertyDescriptor[] props = objWrapper.desribProperties();
		if (props == null || props.length == 0)
			return Collections.emptyMap();
		Map<String, Object> rsMap = new HashMap<>();
		Object val = null;
		for (PropertyDescriptor prop : props) {
			val = objWrapper.getPropertyValue(prop);
			PropertyContext propContext = createPropertyContext(obj, prop);
			if (propertyAcceptor==null || propertyAcceptor.apply(propContext, val)){
				if(valueConvertor!=null){
					Object newVal = valueConvertor.apply(prop, val);
					val = (newVal!=null?newVal:val);
				}
//				PropertyContext propContext = createPropertyContext(obj, prop);
				rsMap.put(toPropertyName(propContext.getConvertedName()), val);
			}
		}
		return rsMap;
	}
	
	protected ObjectWrapper objectWrapper(Object obj) {
		return new DefaultObjectWrapper(obj);
	}
	
	protected PropertyContext createPropertyContext(final Object obj, PropertyDescriptor prop){
		return new PropertyContext(obj, prop, prop.getName());
	}
	
	private String toPropertyName(String propertyName){
		return propertyName;
	}
	
	/***
	 * 递归解释嵌套对象
	 */
	public Map<String, Object> toFlatMap(final Object obj){
		final Map<String, Object> params = new HashMap<>();
		toFlatMap(params, obj);
		return params;
	}
	
	/*public BeanToMapConvertor addValueType(Class<?> clazz){
//		this.checkFreezed();
		this.valueTypes.add(clazz);
		return this;
	}*/
	

	public boolean isMappableValue(Object value){
		if(value==null)
			return true;
//		return valueTypes.contains(value.getClass()) || (flatableObject!=null && !flatableObject.apply(value));
		return (flatableObject!=null && !flatableObject.apply(value));
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
		flatObject(prefix, obj, (k, v, c)->params.put(toPropertyName(k), v));
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
//		PropertyContext ctx = new PropertyContext(obj, null, prefixName);
		flatObject(prefixName==null?"":prefixName, obj, valuePutter, null);
	}
	
	private boolean isMapObject(Object obj){
		return Map.class.isInstance(obj);
	}
	
	private boolean isMultiple(Object obj){
		return LangUtils.isMultiple(obj);
	}
	
	@SuppressWarnings("unchecked")
	private <T> void flatObject(final String prefixName, final Object obj, ValuePutter valuePutter, PropertyContext keyContext){
		Objects.requireNonNull(prefixName);
		if(isMappableValue(obj)){
			Object convertedValue = convertValue(null, obj);
			valuePutter.put(prefixName, convertedValue, keyContext);
		}else if(isMapObject(obj)){
			String mapPrefixName = prefixName;
			if(StringUtils.isNotBlank(prefixName)){
				mapPrefixName = prefixName+this.propertyAccesor;
			}
			for(Entry<String, Object> entry : ((Map<String, Object>)obj).entrySet()){
				if(isMappableValue(entry.getValue())){
					Object convertedValue = convertValue(null, entry.getValue());
					valuePutter.put(mapPrefixName+entry.getKey(), convertedValue, null);
				}else{
					flatObject(mapPrefixName+entry.getKey(), entry.getValue(), valuePutter);
				}
			}
		}else if(isMultiple(obj)){
			List<Object> list = LangUtils.asList(obj);
			int index = 0;
			for(Object o : list){
				String listIndexName = prefixName + this.listOpener+index+this.listCloser;
				if(isMappableValue(o)){
					Object convertedValue = convertValue(null, o);
					valuePutter.put(listIndexName, convertedValue, null);
				}else{
					flatObject(listIndexName, o, valuePutter);
				}
				index++;
			}
		}else{
			/*if(flatableObject==null || !flatableObject.apply(obj)){
				valuePutter.put(prefixName, obj);
				return ;
			}*/
			ObjectWrapper ow = objectWrapper(obj);
			Stream.of(ow.desribProperties()).forEach(prop -> {
//			ReflectUtils.listProperties(obj.getClass(), prop-> {
//				Object val = ReflectUtils.getProperty(obj, prop);
				Object val = ow.getPropertyValue(prop);
//				System.out.println("prefixName:"+prefixName+",class:"+obj.getClass()+", prop:"+prop.getName()+", value:"+val);
				PropertyContext propContext = createPropertyContext(obj, prop);
				if (propertyAcceptor==null || propertyAcceptor.apply(propContext, val)){
					/*if(isMapObject(val) || isMultiple(val)){
						//no convert
					}else if(valueConvertor!=null){
						Object newVal = valueConvertor.apply(prop, val);
						val = (newVal!=null?newVal:val);
					}else if(val instanceof Enum){
						val = ((Enum<?>)val).name();
					}*/
					
					if(StringUtils.isBlank(prefixName)){
						flatObject(propContext.getConvertedName(), val, valuePutter, propContext);
					}else{
						String prefix = prefixName+propertyAccesor;
						propContext.setPrefix(prefix);
						flatObject(prefix+propContext.getConvertedName(), val, valuePutter, propContext);
					}
				}
			});
		}
	}
	
	private Object convertValue(PropertyDescriptor prop, Object val){
		if(val instanceof Enum){
			val = ((Enum<?>)val).name();
		}
		if(valueConvertor!=null){
			Object newVal = valueConvertor.apply(prop, val);
			val = (newVal!=null?newVal:val);
		}
		return val;
	}

	protected static interface ObjectWrapper {
		PropertyDescriptor[] desribProperties();
		Object getPropertyValue(PropertyDescriptor prop);
	}
	
	protected static class DefaultObjectWrapper implements ObjectWrapper {
		final private Object object;

		public DefaultObjectWrapper(Object object) {
			super();
			this.object = object;
		}
		public PropertyDescriptor[] desribProperties() {
			return ReflectUtils.desribProperties(object.getClass());
		}
		public Object getPropertyValue(PropertyDescriptor prop) {
			return ReflectUtils.getProperty(object, prop);
		}
	}
	
	
	public static interface ValuePutter {
		void put(String key, Object value, PropertyContext keyContext);
	}
	
	public class PropertyContext {
		final protected Object source;
		final protected PropertyDescriptor property;
		final protected String name;
		protected String prefix;
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
			/*String name = this.name;
			if(enableFieldNameAnnotation && source!=null){
				FieldName fn = ReflectUtils.getFieldNameAnnotation(source.getClass(), name);
				if(fn!=null){
					name = fn.value();
				}
			}
			if(enableUnderLineStyle){
				name = StringUtils.convert2UnderLineName(name);
			}*/
//			String name = propertyNameConvertor.convert(this);
			return name;
		}
		private String getConvertedName() {
			return propertyNameConvertor.convert(this);
		}
		public String getPrefix() {
			return prefix;
		}
		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}
		
	}


	public static class BeanToMapBuilder extends BaseBeanToMapBuilder<BeanToMapBuilder>{
		public static BeanToMapBuilder newBuilder(){
			return new BeanToMapBuilder();
		}
		
	}
	protected static class BaseBeanToMapBuilder<T extends BaseBeanToMapBuilder<T>> {
//		private BeanToMapConvertor beanToFlatMap = new BeanToMapConvertor();
		protected BiFunction<PropertyContext, Object, Boolean> propertyAcceptor = new DefaultPropertyAcceptor();
		protected BiFunction<PropertyDescriptor, Object, Object> valueConvertor;
		protected Function<Object, Boolean> flatableObject;
		protected boolean enableFieldNameAnnotation = false;
		protected boolean enableUnderLineStyle = false;
		protected String prefix = "";
		protected PropertyNameConvertor propertyNameConvertor;

		public void copyTo(BaseBeanToMapBuilder<?> builder){
			builder.prefix = this.prefix;
			builder.propertyAcceptor = this.propertyAcceptor;
			builder.valueConvertor = this.valueConvertor;
			builder.flatableObject = this.flatableObject;
			builder.enableFieldNameAnnotation = this.enableFieldNameAnnotation;
			builder.enableUnderLineStyle = this.enableUnderLineStyle;
		}
		
		public T propertyAcceptor(BiFunction<PropertyContext, Object, Boolean> propertyAcceptor) {
			this.propertyAcceptor = propertyAcceptor;
			return self();
		}
		public T excludeProperties(String... properties) {
			this.propertyAcceptor = new ExcludePropertyAcceptor(Arrays.asList(properties));
			return self();
		}
		public T propertyNameConvertor(PropertyNameConvertor propertyNameConvertor) {
			this.propertyNameConvertor = propertyNameConvertor;
			return self();
		}

		public T flatableObject(Function<Object, Boolean> flatableObject) {
			this.flatableObject = flatableObject;
			return self();
		}

		public T valueConvertor(BiFunction<PropertyDescriptor, Object, Object> valueConvertor) {
			this.valueConvertor = valueConvertor;
			return self();
		}
		public T enableFieldNameAnnotation() {
			this.enableFieldNameAnnotation = true;
			return self();
		}
		public T enableUnderLineStyle() {
			this.enableUnderLineStyle = true;
			return self();
		}

		public T prefix(String prefix) {
			this.prefix = prefix;
			return self();
		}
		
		@SuppressWarnings("unchecked")
		protected T self(){
			return (T)this;
		}
		
		/*public Map<String, Object> toMap(Object obj){
			return beanToFlatMap.toMap(obj);
		}
		
		public Map<String, Object> toFlatMap(Object obj){
			return beanToFlatMap.toFlatMap(obj);
		}*/
		public BeanToMapConvertor build(){
			BeanToMapConvertor beanToFlatMap = new BeanToMapConvertor();
			beanToFlatMap.setPrefix(prefix);
			beanToFlatMap.setPropertyAcceptor(propertyAcceptor);
			beanToFlatMap.setValueConvertor(valueConvertor);
			if(flatableObject!=null){
				beanToFlatMap.setFlatableObject(flatableObject);
			}
			beanToFlatMap.propertyNameConvertor = new DefaultPropertyNameConvertor(enableFieldNameAnnotation, enableUnderLineStyle);
			return beanToFlatMap;
		}
	}
	
	
}
