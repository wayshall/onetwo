package org.onetwo.common.excel.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class Intro<T> {

	private final static Logger logger = LoggerFactory.getLogger(Intro.class);
	
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
			return Collections.EMPTY_MAP;
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(clazz, Object.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
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
		
		return ImmutableList.copyOf(_allFieldMap.values());
	}
	
	public List<String> getAllPropertyNames() {
		return ImmutableList.copyOf(propertyDescriptors.keySet());
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
				_fieldMaps = Collections.EMPTY_MAP;
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
	
	public Method getReadMethod(String propName) {
		PropertyDescriptor pd = checkProperty(propName);
		
		Method readMethod;
//		if (Serializable.class.equals(pd.getPropertyType())) {
		if (pd.getReadMethod()==null || pd.getReadMethod().isBridge()) {
			String rmethodName = getReadMethodName(pd.getName(), pd.getPropertyType());
			readMethod = findPublicMethod(clazz, rmethodName);
		} else {
			readMethod = pd.getReadMethod();
		}
		return readMethod;
	}
	
	public Object getPropertyValue(Object element, String propName) {
		Method readMethod = getReadMethod(propName);
		if(readMethod==null)
			throw new NoSuchElementException("no read method found, class:"+element.getClass()+", property:"+propName);
		return invokeMethod(true, readMethod, element);
	}

	public PropertyDescriptor checkProperty(String propName){
		PropertyDescriptor pd = getProperty(propName);
		if(pd==null)
			throw new RuntimeException("no property found: " + propName);
		return pd;
	}

	public boolean hasProperty(String propName){
		return propertyDescriptors.containsKey(propName);
	}
	
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
			throw new RuntimeException("no field found: " + fieldName);
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
			f.set(obj, value);
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
//		Assert.isInstanceOf(clazz, obj);
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

	public static List<Method> findPublicMethods(Class objClass, String name, Class... paramTypes) {
		Assert.notNull(objClass, "objClass must not be null");
		Assert.notNull(name, "Method name must not be null");
		List<Method> methodList = new ArrayList<Method>();
		try {
			Method[] methods = objClass.getMethods();
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				if (!method.isBridge() && name.equals(method.getName()) && (ExcelUtils.isEmpty(paramTypes) || matchParameterTypes(paramTypes, method.getParameterTypes())) ) {
					methodList.add(method);
				}
			}
		} catch (Exception e) {
			logger.error("findPublicMethods ["+name+"] method error : "+e.getMessage());
		}
		return methodList;
	}
	
	public static Method findPublicMethod(Class objClass, String name, Class... paramTypes) {
		try {
			return findPublicMethods(objClass, name, paramTypes).get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public static boolean matchParameterTypes(Class[] sourceTypes, Class[] targetTypes) {
		if (sourceTypes.length != targetTypes.length)
			return false;
		int index = 0;
		for (Class cls : targetTypes) {
			if (!cls.isAssignableFrom(sourceTypes[index]))
				return false;
			index++;
		}
		return true;
	}
	public static Object invokeMethod(boolean throwIfError, Method method, Object target, Object... args) {
		try {
			if (!method.isAccessible())
				method.setAccessible(true);
			return method.invoke(target, args);
		} catch (Exception ex) {
			if (throwIfError)
				throw new RuntimeException("invoke target["+target+"] method[" + method + "] error: " + ex.getMessage(), ex);
			else
				return null;
		}
	}
	
	public String toString(){
		return "class wraper["+clazz+"]";
	}
}
