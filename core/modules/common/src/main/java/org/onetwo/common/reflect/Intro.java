package org.onetwo.common.reflect;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.onetwo.common.annotation.AnnotationUtils;
import org.onetwo.common.convert.Types;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.JFishFieldInfoImpl;
import org.onetwo.common.utils.JFishProperty;
import org.onetwo.common.utils.JFishPropertyInfoImpl;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.TypeJudge;
import org.onetwo.common.utils.list.It;
import org.onetwo.common.utils.list.JFishList;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class Intro<T> {
	
	public static <E> Intro<E> wrap(Class<E> clazz){
		Intro<E> intro = new Intro<E>(clazz);
		return intro;
	}


	public static final String JAVASSIST_KEY = "_$$_javassist_";
	public static final String READMETHOD_KEY = "get";
	public static final String BOOLEAN_READMETHOD_KEY = "is";
	public static final String WRITEMETHOD_KEY = "set";
	
	private final Class<T> clazz;
	private final Map<String, PropertyDescriptor> propertyDescriptors;
	private Map<String, Field> _fieldMaps;
	private ReentrantLock _fieldLock = new ReentrantLock();

//	private List<Field> allFields;
	private Map<String, Field> _allFieldMap;
	private ReentrantLock _allFieldLock = new ReentrantLock();

	public Intro(Class<T> clazz) {
		Assert.notNull(clazz);
		this.clazz = clazz;
		this.propertyDescriptors = loadPropertyDescriptors();
//		this.fields = loadFields();
	}

	public Class<?> getClazz() {
		return clazz;
	}
	
	private Map<String, PropertyDescriptor> loadPropertyDescriptors(){
		if(clazz==Object.class || clazz.isInterface() || clazz.isPrimitive())
			return Collections.emptyMap();
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(clazz, Object.class);
		} catch (Exception e) {
			throw new BaseException(e);
		}
		PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
		
		Map<String, PropertyDescriptor> maps = new LinkedHashMap<String, PropertyDescriptor>();
		for(PropertyDescriptor prop : props){
			maps.put(prop.getName(), prop);
		}
		return Collections.unmodifiableMap(maps);
	}
	
	
	public List<Field> getAllFields() {
		_loadAllFields();
		
		return JFishList.wrap(_allFieldMap.values());
	}
	
	public List<String> getAllPropertyNames() {
		return JFishList.wrap(propertyDescriptors.keySet());
	}
	

	@SuppressWarnings("unchecked")
	public List<String> getPropertyNames(final Class<? extends Annotation>... ignoreAnnotation){
		return getPropertyDescriptors(ignoreAnnotation).getPropertyList("name");
	}
	
	@SuppressWarnings("unchecked")
	public JFishList<PropertyDescriptor> getPropertyDescriptors(final Class<? extends Annotation>... ignoreAnnotations){
		return JFishList.wrap(this.propertyDescriptors.values()).filter(new It<PropertyDescriptor>() {
			
			@Override
			public boolean doIt(PropertyDescriptor element, int index) {
				for(Class<? extends Annotation> anno : ignoreAnnotations){
					if(element.getReadMethod().getAnnotation(anno)!=null){
						return false;
					}
				}
				return true;
			}
		});
	}
	
	public Map<String, Field> getAllFieldMap() {
		_loadAllFields();
		return _allFieldMap;
	}

	private void _loadAllFields() {
		if(_allFieldMap!=null)
			return ;
		
		_allFieldLock.lock();
		try {
			if(_allFieldMap!=null)//dbcheck
				return ;

			Map<String, Field> tempMap = Maps.newHashMap(getFieldMaps());
			List<Class<?>> classes = findSuperClasses(clazz);
//			Field[] fs = null;
			for (Class<?> cls : classes) {
				/*fs = cls.getDeclaredFields();
				for (Field f : fs) {
					tempMap.put(f.getName(), f);
				}*/
				tempMap.putAll(Intro.wrap(cls).getAllFieldMap());
			}
			
			this._allFieldMap = ImmutableMap.copyOf(tempMap);
		} finally{
			_allFieldLock.unlock();
		}
	}

	private void _loadFields(){
		if(_fieldMaps!=null)
			return ;
		
		this._fieldLock.lock();
		try{
			if(_fieldMaps!=null)
				return ;

			if(clazz==Object.class || clazz.isPrimitive()){
				_fieldMaps = Collections.emptyMap();
				return ;
			}
			Field[] fields = clazz.getDeclaredFields();
			Map<String, Field> maps = new LinkedHashMap<String, Field>(fields.length);
			for(Field field : fields){
				maps.put(field.getName(), field);
			}
			this._fieldMaps = ImmutableMap.copyOf(maps);
		}finally{
			this._fieldLock.unlock();
		}
		
	}

	public Map<String, Field> getFieldMaps() {
		_loadFields();
		return _fieldMaps;
	}
	
	public Map<String, PropertyDescriptor> getPropertyDescriptors() {
		return propertyDescriptors;
	}

	public Collection<PropertyDescriptor> getProperties(){
		return propertyDescriptors.values();
	}

	public PropertyDescriptor[] getPropertyArray(){
		return propertyDescriptors.values().toArray(new PropertyDescriptor[propertyDescriptors.size()]);
	}

	public PropertyDescriptor getProperty(String propName){
		return propertyDescriptors.get(propName);
	}

	public PropertyDescriptor checkProperty(String propName){
		PropertyDescriptor pd = getProperty(propName);
		if(pd==null)
			throw new BaseException("no property found: " + propName);
		return pd;
	}

	public boolean hasProperty(String propName){
		return propertyDescriptors.containsKey(propName);
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
		return getFieldMaps().values();
	}
	
	public List<Field> getFieldList(boolean parent){
		if(parent){
			return getAllFields();
		}else{
			return new ArrayList<Field>(getFields());
		}
	}
	
	

	public Field getField(String fieldName){
		return getFieldMaps().get(fieldName);
	}
	
	public boolean containsField(String fieldName, boolean includeParent){
		if(includeParent){
			return getAllFieldMap().containsKey(fieldName);
		}else{
			return _fieldMaps.containsKey(fieldName);
		}
	}

	public Field getField(String fieldName, boolean parent){
		if(parent){
			return getAllFieldMap().get(fieldName);
		}else{
			return getFieldMaps().get(fieldName);
		}
	}
	
	public Field getStaticField(String fieldName){
		Field f = getField(fieldName);
		if(Modifier.isStatic(f.getModifiers()))
			return f;
		return null;
	}
	

	public Field checkField(String fieldName, boolean parent){
		Field f = getField(fieldName, parent);
		if(f==null)
			throw new BaseException("no field found: " + fieldName);
		return f;
	}
	

	
	public Object getStaticFieldValue(String fieldName, boolean parent) {
		Field f = checkField(fieldName, parent);
		return getFieldValue(f, clazz, true);
	}
	
	public static Object getFieldValue(Field f, Object obj, boolean throwIfError) {
		Assert.notNull(f);
		try {
			if (!f.isAccessible())
				f.setAccessible(true);
			return f.get(obj);
		} catch (Exception ex) {
			if (throwIfError)
				throw new RuntimeException("get value of field[" + f + "] error: " + ex.getMessage(), ex);
			else
				return null;
		}
	}
	
	public static void setFieldValue(Field f, Object obj, Object value) {
		Assert.notNull(f);
		try {
			if (!f.isAccessible())
				f.setAccessible(true);
			f.set(obj, value==null?null:Types.convertValue(value, f.getType()));
		} catch (Exception ex) {
			throw new RuntimeException("invoke method error: " + ex.getMessage(), ex);
		}
	}
	public static <T> T newInstance(Class<T> clazz) {
		T instance = null;
		try {
			instance = clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("instantce class error : " + clazz, e);
		}
		return instance;
	}

	public Object getFieldValue(Object obj, String fieldName) {
		return getFieldValue(obj, fieldName, true);
	}
	public Object getFieldValue(Object obj, String fieldName, boolean parent) {
		Assert.isInstanceOf(clazz, obj);
		Field f = checkField(fieldName, parent);
		return getFieldValue(f, obj, true);
	}
	

	public void setFieldValue(Object obj, String fieldName, Object value) {
		setFieldValue(obj, fieldName, value, true);
	}
	public void setFieldValue(Object obj, String fieldName, Object value, boolean parent) {
		Assert.isInstanceOf(clazz, obj);
		Field field = checkField(fieldName, parent);
		setFieldValue(field, obj, value);
	}
	

	public void setStaticFieldValue(Object value, String fieldName) {
		setStaticFieldValue(value, fieldName, true);
	}
	public void setStaticFieldValue(Object value, String fieldName, boolean parent) {
		Field field = checkField(fieldName, parent);
		setFieldValue(field, clazz, value);
	}

	public T newInstance(){
		T bean = (T)newInstance(clazz);
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
	public T newFrom(Map<?, ?> propValues){
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
	
	public Object setPropertyValue(Object element, String propName, Object value) {
		Method writeMthod = getWriteMethod(propName);
		if(writeMthod==null)
			throw new NoSuchElementException("no write method found, class:"+element.getClass()+", property:"+propName);
		return ReflectUtils.invokeMethod(writeMthod, element, value);
	}
	
	public Object setPropertyValue(Object element, PropertyDescriptor pd, Object value) {
		Method writeMthod = pd.getWriteMethod();
		if(writeMthod==null)
			throw new NoSuchElementException("no write method found, class:"+element.getClass()+", property:"+pd.getName());
		return ReflectUtils.invokeMethod(writeMthod, element, value);
	}
	
	public Object getPropertyValue(Object element, String propName) {
		Method readMethod = getReadMethod(propName);
		if(readMethod==null)
			throw new NoSuchElementException("no read method found, class:"+element.getClass()+", property:"+propName);
		return ReflectUtils.invokeMethod(readMethod, element);
	}
	
	public Object getPropertyValue(Object element, PropertyDescriptor pd) {
		Method readMethod = pd.getReadMethod();
		if(readMethod==null)
			throw new NoSuchElementException("no read method found, class:"+element.getClass()+", property:"+pd.getName());
		return ReflectUtils.invokeMethod(readMethod, element);
	}
	
	public Method getReadMethod(String propName) {
		PropertyDescriptor pd = checkProperty(propName);
		
		Method readMethod;
//		if (Serializable.class.equals(pd.getPropertyType())) {
		if (pd.getReadMethod()==null || pd.getReadMethod().isBridge()) {
			String rmethodName = getReadMethodName(pd.getName(), pd.getPropertyType());
			readMethod = ReflectUtils.findPublicMethod(clazz, rmethodName);
		} else {
			readMethod = pd.getReadMethod();
		}
		return readMethod;
	}


	public Method getWriteMethod(String propName) {
		PropertyDescriptor pd = checkProperty(propName);
		
		Method writeMethod;
		if (pd.getWriteMethod()==null || pd.getWriteMethod().isBridge()) {
			String wmethodName = getWriteMethodName(pd.getName());
			writeMethod = ReflectUtils.findPublicMethod(clazz, wmethodName);
		} else {
			writeMethod = pd.getWriteMethod();
		}
		return writeMethod;
	}
	
	public static String getReadMethodName(String propName, Class<?> returnType) {
		String getMethod = StringUtils.capitalize(propName);
		if (returnType!=null && Boolean.class.equals(returnType) || boolean.class.equals(returnType)) {
			getMethod = BOOLEAN_READMETHOD_KEY + getMethod;
		} else {
			getMethod = READMETHOD_KEY + getMethod;
		}
		return getMethod;
	}
	
	public static String getWriteMethodName(String name) {
		String getMethod = StringUtils.capitalize(name);
		return WRITEMETHOD_KEY + getMethod;
	}

	public Collection<String> desribPropertyNames() {
		return this.propertyDescriptors.keySet();
	}

	public boolean isCollectionType(){
		return Collection.class.isAssignableFrom(clazz);
	}

	public boolean isMapType(){
		return Map.class.isAssignableFrom(clazz);
	}
	
	public void copy(Object source, Object target, PropertyCopyer<PropertyDescriptor> copyer){
		Collection<String> sourceProperties = ReflectUtils.getIntro(source.getClass()).desribPropertyNames();
		for(PropertyDescriptor targetProperty : this.propertyDescriptors.values()){//propertyDescriptors is target
			if(sourceProperties.contains(targetProperty.getName()))
				copyer.copy(source, target, targetProperty);
		}
	}
	
	public List<Class<?>> findSuperClasses() {
		return findSuperClasses(Object.class);
	}

	public List<Class<?>> findSuperClasses(Class<?> stopClass) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		Class<?> parent = clazz.getSuperclass();
		while (parent != null && !parent.equals(stopClass)) {
			classes.add(parent);
			parent = parent.getSuperclass();
		}
		return classes;
	}

	public Collection<Field> getNotStaticAndTransientFields(boolean parent) {
		Collection<Field> fields = getFieldList(false);
		Collection<Field> rsfields = LangUtils.newArrayList();
		for (Field f : fields) {
			if(Modifier.isTransient(f.getModifiers()) || Modifier.isStatic(f.getModifiers()))
				continue;
			rsfields.add(f);
		}
		return rsfields;
	}
	
	public <E extends Annotation> E getAnnotationWithParent(Class<E> annotationClass) {
		return AnnotationUtils.findAnnotationWithParent(clazz, annotationClass);
	}

	public <E extends Annotation> E getAnnotationWithSupers(Class<E> annotationClass) {
		return AnnotationUtils.findAnnotationWithSupers(clazz, annotationClass);
	}

	public <E extends Annotation> E getAnnotationWithInterfaces(Class<E> annotationClass) {
		return AnnotationUtils.findAnnotationWithInterfaces(clazz, annotationClass);
	}
	
	public String toString(){
		return "class wraper["+clazz+"]";
	}
}
