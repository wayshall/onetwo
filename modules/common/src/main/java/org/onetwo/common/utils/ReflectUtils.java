package org.onetwo.common.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.WeakHashMap;

import org.apache.log4j.Logger;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.delegate.DelegateFactory;
import org.onetwo.common.utils.delegate.DelegateMethod;
import org.onetwo.common.utils.list.ListFun;
import org.onetwo.common.utils.map.BaseMap;
import org.onetwo.common.utils.map.M;

@SuppressWarnings( { "rawtypes", "unchecked" })
public class ReflectUtils {

	private static Logger logger = Logger.getLogger(ReflectUtils.class);

	public static final Class[] EMPTY_CLASS_ARRAY = new Class[0];
	private static WeakHashMap<Class, PropertyDescriptor[]> DESCRIPTORS_CACHE = new WeakHashMap<Class, PropertyDescriptor[]>();
	private static WeakHashMap<Class, List<Field>> FIELDS_CACHE = new WeakHashMap<Class, List<Field>>();
	
	public static final String READMETHOD_KEY = "get";
	public static final String BOOLEAN_READMETHOD_KEY = "is";
	public static final String WRITEMETHOD_KEY = "set";
	
	private static final BaseMap<Class<?>, Class<?>> BASE_TYPE_MAPPER;
	
	static {

		BASE_TYPE_MAPPER = new BaseMap<Class<?>, Class<?>>();
		BASE_TYPE_MAPPER.put(boolean.class, Boolean.class);
		BASE_TYPE_MAPPER.put(char.class, Character.class);
		BASE_TYPE_MAPPER.put(byte.class, Byte.class);
		BASE_TYPE_MAPPER.put(short.class, Short.class);
		BASE_TYPE_MAPPER.put(int.class, Integer.class);
		BASE_TYPE_MAPPER.put(long.class, Long.class);
		BASE_TYPE_MAPPER.put(float.class, Float.class);
		BASE_TYPE_MAPPER.put(double.class, Double.class);
	}

	private ReflectUtils() {
	}
	
	public static interface PropertyDescriptorCallback {
		void doWithProperty(PropertyDescriptor propertyDescriptor);
	}
	
	public static <T, ID> void mergeByCheckedIds(
			final Collection<T> srcObjects, final Collection<ID> checkedIds,
			final Class<T> clazz, final String idName) {

		Assert.notNull(srcObjects, "srcObjects不能为null");
		Assert.hasText(idName, "idName不能为null");
		Assert.notNull(clazz, "clazz不能为null");

		if (checkedIds == null) {
			srcObjects.clear();
			return;
		}

		Iterator<T> srcIterator = srcObjects.iterator();
		try {

			while (srcIterator.hasNext()) {
				T element = srcIterator.next();
				Object id;
				id = getProperty(element, idName);

				if (!checkedIds.contains(id)) {
					srcIterator.remove();
				} else {
					checkedIds.remove(id);
				}
			}

			for (ID id : checkedIds) {
				T obj = clazz.newInstance();
				setProperty(obj, idName, id);
				srcObjects.add(obj);
			}
		} catch (Exception e) {
			handleReflectionException(e);
		}
	}

	public static <T> Collection<T> getProperties(Collection elements, String propName) {
		Collection<T> values = new ArrayList<T>(elements.size());
		T val = null;
		for(Object obj : elements){
			val = (T)getProperty(obj, propName);
			values.add(val);
		}
		return values;
	}

	public static Object getProperty(Object element, String propName) {
		return getProperty(element, propName, true);
	}

	public static Object getProperty(Object element, String propName,
			boolean throwIfError) {
		if (element instanceof Map) {
			return getValue((Map) element, propName);
		}
		PropertyDescriptor prop = getPropertyDescriptor(element, propName);
		Object value = null;
		if (prop != null)
			value = invokeMethod(throwIfError, getReadMethod(
					element.getClass(), prop), element);
		return value;
	}

	public static Object getProperty(Object element, PropertyDescriptor prop) {
		return invokeMethod(false, ReflectUtils.getReadMethod(element
				.getClass(), prop), element);
	}

	public static void setProperty(Object element, PropertyDescriptor prop, Object val) {
		invokeMethod(getWriteMethod(element.getClass(), prop), element, LangUtils.tryCastTo(val, prop.getPropertyType()));
	}

	protected static Object getValue(Map map, String propName) {
		return ((Map) map).get(propName);
	}

	public static void setProperty(Object element, String propName, Object value) {
		setProperty(element, propName, value, true);
	}

	public static void setProperty(Object element, String propName,
			Object value, boolean throwIfError) {
		try {
			if (element instanceof Map) {
				((Map) element).put(propName, value);
				return;
			}
			PropertyDescriptor prop = getPropertyDescriptor(element, propName);
			if (prop == null)
				throw new BaseException("can not find the property : " + propName);
			if (prop.getPropertyType().isPrimitive() && value == null) {
				LangUtils.throwBaseException("the property[" + propName
						+ "] type is primitive[" + prop.getPropertyType()
						+ "], the value can not be null");
			}
			invokeMethod(prop.getWriteMethod(), element, value);
		} catch (Exception e) {
			if (throwIfError)
				handleReflectionException(e);
		}
	}

	public static void tryToSetProperty(Object element, String propName,
			Object value) {
		boolean exp = false;
		try {
			setProperty(element, propName, value, true);
		} catch (Exception e) {
			exp = true;
		}
		if (exp) {
			exp = false;
			try {
				String setMethodName = "set"
						+ propName.substring(0, 1).toUpperCase()
						+ propName.substring(1);
				invokeMethod(setMethodName, element, value);
			} catch (Exception e) {
				exp = true;
			}
		}
		if (exp) {
			exp = false;
			try {
				setFieldValue(propName, element, value);
			} catch (Exception e) {
				exp = true;
			}
		}

		if (exp)
			throw new ServiceException("can not set the property[" + propName
					+ "]'s value");
	}

	public static PropertyDescriptor getPropertyDescriptor(Object element, String propName) {
		return getPropertyDescriptor(element.getClass(), propName);
	}

	public static PropertyDescriptor getPropertyDescriptor(Class<?> element, String propName) {
		PropertyDescriptor[] props = desribProperties(element);
		for (PropertyDescriptor prop : props) {
			if (prop.getName().equals(propName))
				return prop;
		}
		return null;
	}
	
	public static boolean isPropertyOf(Object bean, String propName){
		if(bean instanceof Map){
			return ((Map) bean).containsKey(propName);
		}else{
			return getProperty(bean, propName)!=null;
		}
	}

	public static List convertElementPropertyToList(
			final Collection collection, final String propertyName) {
		if (collection == null || collection.isEmpty())
			return null;
		List list = new ArrayList();

		try {
			for (Object obj : collection) {
				if (obj == null)
					continue;
				list.add(getProperty(obj, propertyName));
			}
		} catch (Exception e) {
			handleReflectionException(e);
		}

		return list;
	}

	public static String convertElementPropertyToString(
			final Collection collection, final String propertyName,
			final String separator) {
		List list = convertElementPropertyToList(collection, propertyName);
		return StringUtils.join(list, separator);
	}

	public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
		return getSuperClassGenricType(clazz, Object.class);
	}

	public static <T> Class<T> getSuperClassGenricType(final Class clazz,
			final Class stopClass) {
		return getSuperClassGenricType(clazz, 0, stopClass);
	}

	/***************************************************************************
	 * GenricType handler, copy form spring-side
	 * 
	 * @param clazz
	 * @param index
	 * @param stopClass
	 * @return
	 */
	public static Class getSuperClassGenricType(final Class clazz,
			final int index, final Class stopClass) {
		if(clazz.equals(stopClass))
			return clazz;
		
		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			if (stopClass.isAssignableFrom((Class) genType)) {
				while (!(genType instanceof ParameterizedType)) {
					genType = ((Class) genType).getGenericSuperclass();
					if (genType == null)
						return Object.class;
				}
			} else {
				logger.warn(clazz.getSimpleName()
						+ "'s superclass not ParameterizedType");
				return Object.class;
			}
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			logger.warn("Index: " + index + ", Size of "
					+ clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger
					.warn(clazz.getSimpleName()
							+ " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class) params[index];
	}

	public static Class getGenricType(final Object obj, final int index) {

		Class clazz = obj.getClass();
		Type genType = (Type) clazz;

		if (!(genType instanceof ParameterizedType)) {
			logger.warn(clazz.getSimpleName()
					+ "'s class not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			logger.warn("Index: " + index + ", Size of "
					+ clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(clazz.getSimpleName()
					+ " not set the actual class on class generic parameter");
			return Object.class;
		}

		return (Class) params[index];
	}

	public static Class getListGenricType(final Class clazz) {
		Class genClass = null;
		if (List.class.isAssignableFrom(clazz)) {
			Method method = findMethod(clazz, "get", int.class);
			if (method != null) {
				Type rtype = method.getGenericReturnType();
				if (ParameterizedType.class.isAssignableFrom(rtype.getClass())) {
					ParameterizedType ptype = (ParameterizedType) rtype;
					genClass = (Class) ptype.getActualTypeArguments()[0];
				}
			}
		}
		return genClass;
	}

	public static Method findMethod(Class objClass, String name, Class... paramTypes) {
		return findMethod(false, objClass, name, paramTypes);
	}

	public static Method getReadMethod(Class objClass, PropertyDescriptor pd) {
		Method readMethod;
//		if (Serializable.class.equals(pd.getPropertyType())) {
		if (pd.getReadMethod()==null || pd.getReadMethod().isBridge()) {
			readMethod = getReadMethod(objClass, pd.getName(), pd.getPropertyType());
		} else {
			readMethod = pd.getReadMethod();
		}
		return readMethod;
	}

	public static Method getWriteMethod(Class objClass, PropertyDescriptor pd) {
		Method writeMethod;
		if (pd.getWriteMethod()==null || pd.getWriteMethod().isBridge()) {
			writeMethod = getWriteMethod(objClass, pd.getName());
		} else {
			writeMethod = pd.getWriteMethod();
		}
		return writeMethod;
	}

	public static Method getReadMethod(Class objClass, String propName, Class returnType) {
		String mName = getReadMethodName(propName, returnType);
		return findPublicMethod(objClass, mName);
	}

	public static Method getWriteMethod(Class objClass, String propName) {
		String mName = getWriteMethodName(propName);
		return findPublicMethod(objClass, mName);
	}

	public static String getReadMethodName(String name, Class returnType) {
		String getMethod = StringUtils.capitalize(name);
		if (returnType!=null && Boolean.class.equals(returnType)
				|| boolean.class.equals(returnType)) {
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

	public static Method findGetMethod(Class objClass, Field field) {
		String mName = StringUtils.capitalize(field.getName());
		if (field.getType().equals(Boolean.class)
				|| field.getType().equals(boolean.class))
			mName = BOOLEAN_READMETHOD_KEY + mName;
		else
			mName = READMETHOD_KEY + mName;
		return findMethod(false, objClass, mName);
	}

	public static Method findMethod(boolean ignoreIfNotfound, Class objClass, String name, Class... paramTypes) {
		Assert.notNull(objClass, "objClass must not be null");
		Assert.notNull(name, "Method name must not be null");
		try {
			Class searchType = objClass;
			while (!Object.class.equals(searchType) && searchType != null) {
				Method[] methods = (searchType.isInterface() ? searchType
						.getMethods() : searchType.getDeclaredMethods());
				for (int i = 0; i < methods.length; i++) {
					Method method = methods[i];
					// System.out.println("====>>>method:"+method+" parent: " +
					// method.isBridge());
					// if (name.equals(method.getName()) && (paramTypes == null
					// || Arrays.equals(paramTypes,
					// method.getParameterTypes()))) {
					if (!method.isBridge() && name.equals(method.getName()) && (LangUtils.isEmpty(paramTypes) || matchParameterTypes(paramTypes, method.getParameterTypes()))) {
						// System.out.println("====>>>method match");
						return method;
					}
				}
				searchType = searchType.getSuperclass();
			}
		} catch (Exception e) {
			if (ignoreIfNotfound)
				return null;
			handleReflectionException(e);
		}
		if (ignoreIfNotfound)
			return null;
		throw new BaseException("can not find the method : [class=" + objClass + ", methodName=" + name + ", paramTypes=" + LangUtils.toString(paramTypes) + "]");
	}

	/***********
	 * find from declared methods in class or parent class
	 * 
	 * @param objClass
	 * @param name
	 * @return
	 */
	public static List<Method> findMethodsByName(Class objClass, String name, Class... paramTypes) {
		Assert.notNull(objClass, "objClass must not be null");
		Assert.notNull(name, "Method name must not be null");
		List<Method> methodList = new ArrayList<Method>();
		try {
			Class searchType = objClass;
			while (!Object.class.equals(searchType) && searchType != null) {
				Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
				for (int i = 0; i < methods.length; i++) {
					Method method = methods[i];
					if (!method.isBridge() && name.equals(method.getName()) && (LangUtils.isEmpty(paramTypes) || matchParameterTypes(paramTypes, method.getParameterTypes())) ) {
						methodList.add(method);
					}
				}
				searchType = searchType.getSuperclass();
			}
		} catch (Exception e) {
			logger.error("findMethodsByName ["+name+"] error : "+e.getMessage());
		}
		return methodList;
	}

	/***********
	 * 
	 * find from public methods
	 * 
	 * @param objClass
	 * @param name
	 * @return
	 */
	public static List<Method> findPublicMethods(Class objClass, String name, Class... paramTypes) {
		Assert.notNull(objClass, "objClass must not be null");
		Assert.notNull(name, "Method name must not be null");
		List<Method> methodList = new ArrayList<Method>();
		try {
			Method[] methods = objClass.getMethods();
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				if (!method.isBridge() && name.equals(method.getName()) && (LangUtils.isEmpty(paramTypes) || matchParameterTypes(paramTypes, method.getParameterTypes())) ) {
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

	public static List<Method> findAnnotationMethods(Class objClass,
			Class<? extends Annotation> annoClasses) {
		Assert.notNull(annoClasses);
		List<Method> methodList = null;
		try {
			Method[] methods = objClass.getMethods();
			for (Method method : methods) {
				if (method.getAnnotation(annoClasses) == null)
					continue;
				if (methodList == null)
					methodList = new ArrayList<Method>();
				methodList.add(method);
			}
		} catch (Exception e) {
			handleReflectionException(e);
		}
		return methodList == null ? Collections.EMPTY_LIST : methodList;
	}

	public static Method findUniqueAnnotationMethod(Class objClass,
			Class<? extends Annotation> annoClasses, boolean throwIfMore) {
		Assert.notNull(annoClasses);
		Method method = null;
		try {
			Method[] methods = objClass.getMethods();
			for (Method m : methods) {
				if (m.getAnnotation(annoClasses) == null)
					continue;
				if (method != null && throwIfMore)
					throw new ServiceException("the class[" + objClass
							+ "] has more than one method has the annotaiton["
							+ annoClasses + "]");
				method = m;
			}
		} catch (Exception e) {
			handleReflectionException(e);
		}
		return method;
	}

	/***************************************************************************
	 * reflectionException handle, copy from spring
	 * 
	 * @param ex
	 */
	public static void handleReflectionException(Exception ex) {
		if (ex instanceof NoSuchMethodException) {
			throw new IllegalStateException("Method not found: "
					+ ex.getMessage());
		}
		if (ex instanceof IllegalAccessException) {
			throw new IllegalStateException("Could not access method: "
					+ ex.getMessage());
		}
		if (ex instanceof InvocationTargetException) {
			handleInvocationTargetException((InvocationTargetException) ex);
		}
		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		}
		handleUnexpectedException(ex);
	}

	public static void handleInvocationTargetException(
			InvocationTargetException ex) {
		rethrowRuntimeException(ex.getTargetException());
	}

	public static void rethrowRuntimeException(Throwable ex) {
		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		}
		if (ex instanceof Error) {
			throw (Error) ex;
		}
		handleUnexpectedException(ex);
	}

	private static void handleUnexpectedException(Throwable ex) {
		IllegalStateException isex = new IllegalStateException(
				"Unexpected exception thrown");
		isex.initCause(ex);
		throw isex;
	}

	public static <T> T newInstance(Class<T> clazz) {
		T instance = null;
		try {
			instance = clazz.newInstance();
		} catch (Exception e) {
			throw new ServiceException("instantce class error : " + clazz, e);
		}
		return instance;
	}
	

	public static Class<?> loadClass(String className) {
		return loadClass(className, true);
	}

	public static Class<?> loadClass(String className, boolean throwIfError) {
		Class<?> clz = null;
		try {
//			clz = Class.forName(className);
			clz = Class.forName(className, true, ClassUtils.getDefaultClassLoader());
		} catch (Exception e) {
			if(throwIfError)
				throw new ServiceException("class not found : " + className, e);
			else
				logger.warn("class not found : " + className);
		}
		return clz;
	}

	public static Class<?> loadClass(ClassLoader cl, String className) {
		Class<?> clz = null;
		try {
			clz = cl.loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new ServiceException("class not found : " + className, e);
		}
		return clz;
	}

	public static <T> T newInstance(String className) {
		return (T) newInstance(loadClass(className));
	}

	public static <T> List<T> toInstanceList(String clsNames) {
		if (StringUtils.isBlank(clsNames))
			return Collections.EMPTY_LIST;
		String[] cls = StringUtils.split(clsNames, ",");
		T inst = null;
		List list = new ArrayList();
		for (String c : cls) {
			inst = ReflectUtils.newInstance(c);
			if (inst != null)
				list.add(inst);
		}
		return list;
	}

	public static <T> T newInstance(Class<T> clazz, Object... objects) {
		T instance = null;
		try {
			Constructor<T>[] constructs = (Constructor<T>[]) clazz
					.getDeclaredConstructors();
			boolean appropriateConstractor = false;
			for (Constructor<T> constr : constructs) {
				if (matchConstructor(constr, objects)) {
					constr.setAccessible(true);
					instance = constr.newInstance(objects);
					appropriateConstractor = true;
					break;
				}
			}
			if (appropriateConstractor == false && instance == null) {
				StringBuilder sb = new StringBuilder(
						"can not find the appropriate constructor, class: ")
						.append(clazz.getName()).append(", args: ");
				if (objects != null) {
					for (Object o : objects)
						sb.append(o.getClass().getName()).append(" ");
				}
				throw new ServiceException(sb.toString());
			}
		} catch (Exception e) {
			throw new ServiceException("instantce class error : " + clazz, e);
		}
		return instance;
	}

	public static boolean matchConstructor(Constructor constr,
			Object... objects) {
		Class[] pclass = constr.getParameterTypes();
		if (objects.length != pclass.length)
			return false;
		int index = 0;
		for (Class cls : pclass) {
			if (!hasImplements(objects[index], cls))
				return false;
			index++;
		}
		return true;
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

	public static boolean matchParameters(Class[] paramTypes, Object[] params) {
		if(LangUtils.isEmpty(params)){
			 return LangUtils.isEmpty(paramTypes);
		}
		if (paramTypes.length != params.length)
			return false;
		int index = 0;
		for (Class cls : paramTypes) {
			if (!cls.isInstance(params[index])){
				if(cls.isPrimitive() && BASE_TYPE_MAPPER.get(cls).isInstance(params[index])){
					return true;
				}else{
					return false;
				}
			}
			index++;
		}
		return true;
	}
	
	public static Class<?> getBoxingType(Class<?> primitiveType){
		if(!primitiveType.isPrimitive())
			return primitiveType;
		return BASE_TYPE_MAPPER.get(primitiveType);
	}

	public static boolean hasImplements(Object obj, Class clazz) {
		return clazz.isAssignableFrom(obj.getClass());
	}

	public static PropertyDescriptor findProperty(Class<?> clazz,
			String propName) {
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(clazz, Object.class);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		for (PropertyDescriptor prop : beanInfo.getPropertyDescriptors()) {
			if (propName.equals(prop.getName()))
				return prop;
		}
		return null;
	}

	public static PropertyDescriptor[] desribProperties(Class<?> clazz) {
		PropertyDescriptor[] props = DESCRIPTORS_CACHE.get(clazz);
		if (props != null)
			return props;
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(clazz, Object.class);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		props = beanInfo.getPropertyDescriptors();
		if (props != null) {
			DESCRIPTORS_CACHE.put(clazz, props);
		}
		return props;
	}


	public static Map toMap(Object obj) {
		return toMap(true, obj);
	}
	public static Map toMap(boolean ignoreNull, Object obj) {
		if (obj == null)
			return Collections.EMPTY_MAP;
		PropertyDescriptor[] props = desribProperties(obj.getClass());
		if (props == null || props.length == 0)
			return Collections.EMPTY_MAP;
		Map rsMap = new HashMap();
		Object val = null;
		for (PropertyDescriptor prop : props) {
			val = getProperty(obj, prop);
			if (val != null){
				rsMap.put(prop.getName(), val.toString());
			}else{
				if(!ignoreNull)
					rsMap.put(prop.getName(), val);
			}
		}
		return rsMap;
	}

	public static Map<String, String> toStringMap(boolean ignoreNull, Object obj) {
		if (obj == null)
			return Collections.EMPTY_MAP;
		PropertyDescriptor[] props = desribProperties(obj.getClass());
		if (props == null || props.length == 0)
			return Collections.EMPTY_MAP;
		Map<String, String> rsMap = new HashMap<String, String>();
		Object val = null;
		for (PropertyDescriptor prop : props) {
			val = getProperty(obj, prop);
			if (val != null){
				rsMap.put(prop.getName(), val.toString());
			}else{
				if(!ignoreNull)
					rsMap.put(prop.getName(), "");
			}
		}
		return rsMap;
	}

	public static void listProperties(Class objClass, PropertyDescriptorCallback pdcb) {
		Assert.notNull(objClass);
		PropertyDescriptor[] props = desribProperties(objClass);
		if (props == null || props.length == 0)
			return ;
		for (PropertyDescriptor prop : props) {
			pdcb.doWithProperty(prop);
		}
	}
	
	public static <T> T fromMap(Map<String, Object> map, Class<T> beanClass){
		T bean = newInstance(beanClass);
		if(LangUtils.isEmpty(map))
			return bean;
		for(Map.Entry<String, Object> entry : (Set<Map.Entry<String, Object>>) map.entrySet()){
			setExpr(bean, entry.getKey(), entry.getValue());
		}
		return bean;
	}

	public static void copyFields(Object src, Object dest, char seperator, boolean ignoreNull){
		Map<String, String> mappedFields = ReflectUtils.mappedFields(src.getClass(), dest.getClass(), seperator);
		Object fvalue = null;
		for(Entry<String, String> field : mappedFields.entrySet()){
			fvalue = getFieldValue(src, field.getKey());
			if(fvalue==null && ignoreNull)
				continue;
			setFieldValue(field.getValue(), dest, fvalue);
		}
	}
	
	public static void copy(Object source, Object target) {
		copy(source, target, true);
	}

	public static void copy(Object source, Object target, boolean throwIfError) {
		if (source == null)
			return;
		Collection<String> propNames = null;
		if (target instanceof Map) {
			propNames = getPropertiesName(source);
		} else {
			propNames = CollectionUtils.intersection(getPropertiesName(source), getPropertiesName(target));//交集
		}
		Object value = null;
		try {
			for (String prop : propNames) {
				value = getProperty(source, prop);
				setProperty(target, prop, value);
			}
		} catch (Exception e) {
			if (throwIfError)
				LangUtils.throwBaseException(e);
		}
	}

	public static Map field2Map(Object obj) {
		if (obj == null)
			return Collections.EMPTY_MAP;
		Collection<Field> fields = findFieldsFilterStatic(obj.getClass());
		if (fields == null || fields.isEmpty())
			return Collections.EMPTY_MAP;
		Map rsMap = new HashMap();
		Object val = null;
		for (Field field : fields) {
			val = getFieldValue(field, obj, false);
			if (val != null)
				rsMap.put(field.getName(), val);
		}
		return rsMap;
	}

	public static List<PropertyDescriptor> desribProperties(Class<?> clazz,
			Class<? extends Annotation> excludeAnnoClass) {
		PropertyDescriptor[] props = desribProperties(clazz);
		List<PropertyDescriptor> propList = new ArrayList<PropertyDescriptor>();
		Method method = null;
		for (PropertyDescriptor prop : props) {
			method = prop.getReadMethod();
			if (method == null
					|| method.getAnnotation(excludeAnnoClass) != null)
				continue;
			propList.add(prop);
		}
		return propList;
	}

	public static Collection<Field> findFieldsFilterStatic(Class clazz) {
		return findFieldsExcludeAnnotationStatic(clazz, null);
	}

	public static Collection<String> findInstanceFieldNames(Class clazz) {
		return findInstanceFieldNames(clazz, List.class);
	}

	public static Collection<String> findInstanceFieldNames(Class clazz, Class<? extends Collection> collectionClass) {
		Collection<String> fieldNames = newCollections(collectionClass);
		Collection<Field> fields = findFieldsFilterStatic(clazz);
		for(Field f :fields){
			fieldNames.add(f.getName());
		}
		return fieldNames;
	}
	
	public static <T extends Collection> T newCollections(Class<T> clazz){
		if(clazz==List.class){
			return (T)new ArrayList();
		}else if(clazz==Set.class){
			return (T) new HashSet();
		}else if(clazz==SortedSet.class || clazz==NavigableSet.class){
			return (T) new TreeSet();
		}else if(clazz==Queue.class || clazz==Deque.class){
			return (T) new ArrayDeque();
		}else{
			return newInstance(clazz);
		}
	}

	public static Collection<Field> findFieldsExcludeAnnotationStatic(
			Class clazz, Class<? extends Annotation> excludeAnnotation) {
		return findFieldsExclude(clazz, new Class[] { excludeAnnotation },
				"static ", "transient ", ".this$");
	}

	public static Collection<Field> findFieldFilter(Class clazz, String... filterString) {
		return findFieldsExclude(clazz, null, filterString);
	}

	public static Collection<Field> findNotStaticAndTransientFields(Class clazz) {
		List<Class> classes = findSuperClasses(clazz);
		classes.add(0, clazz);

		Collection<Field> fields = new HashSet<Field>();
		Field[] fs = null;
		for (Class cls : classes) {
			fs = cls.getDeclaredFields();
			for (Field f : fs) {
				if(Modifier.isTransient(f.getModifiers()) || Modifier.isStatic(f.getModifiers()))
					continue;
				fields.add(f);
			}
		}
		return fields;
	}

	public static Collection<Field> findFieldsExclude(Class clazz,
			Class<? extends Annotation>[] excludeAnnoClasses,
			String... filterString) {
		List<Class> classes = findSuperClasses(clazz);
		classes.add(0, clazz);

		Collection<Field> fields = new HashSet<Field>();
		Field[] fs = null;
		for (Class cls : classes) {
			fs = cls.getDeclaredFields();
			for (Field f : fs) {
				/*
				 * if(f.toGenericString().indexOf("class$Lcom$yooyo$zjk$BaseEntity")!=-1){
				 * System.out.println("test:"+f.toGenericString()); }
				 */
				if (filterString != null
						&& StringUtils.indexOfAny(f.toString(), filterString) != -1)
					continue;
				if (excludeAnnoClasses != null) {
					for (Class<? extends Annotation> annoCls : excludeAnnoClasses) {
						if (annoCls == null || f.getAnnotation(annoCls) != null)
							continue;
					}
				}
				fields.add(f);
			}
		}
		return fields;
	}

	public static Map toMap(Object objs, String keyName) {
		return toMap(objs, keyName, null);
	}

	public static Map toMap(Object objs, String keyName, String valName) {
		List datas = LangUtils.asList(objs);
		if (datas == null || datas.isEmpty())
			return Collections.EMPTY_MAP;
		Object key;
		Map results = new HashMap();
		for (Object obj : datas) {
			key = getExpr(obj, keyName);
			if (key != null) {
				results.put(key, StringUtils.isBlank(valName) ? obj : getExpr(
						obj, valName));
			}
		}
		return results;
	}


	public static Field findDeclaredField(Class clazz, String fieldName) {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (Exception e) {
			logger.warn("findDeclaredField error : "+e.getMessage());
		}
		return null;
	}

	public static Field findField(Class clazz, String fieldName) {
		return findField(clazz, fieldName, false);
	}

	public static Field findField(Class clazz, String fieldName, boolean throwIfNotfound) {
		List<Field> fields = findAllFields(clazz);
		for (Field f : fields) {
			if (f.getName().equals(fieldName))
				return f;
		}
		if (throwIfNotfound)
			throw new ServiceException("can not find class[" + clazz
					+ "]'s field [" + fieldName + "]");
		return null;
	}

	public static List<Field> findAllFields(Class clazz) {
		List<Field> fields = FIELDS_CACHE.get(clazz);
		if(LangUtils.isNotEmpty(fields))
			return fields;
		
		List<Class> classes = findSuperClasses(clazz);
		classes.add(0, clazz);

		Field[] fs = null;
		fields = new ArrayList<Field>();
		for (Class cls : classes) {
			fs = cls.getDeclaredFields();
			for (Field f : fs) {
				fields.add(f);
			}
		}
		
		FIELDS_CACHE.put(clazz, fields);
		
		return fields;
	}

	public static Field findField(Class clazz, Class fieldType,
			boolean throwIfNotfound) {
		List<Class> classes = findSuperClasses(clazz);
		classes.add(0, clazz);

		Field[] fs = null;
		for (Class cls : classes) {
			fs = cls.getDeclaredFields();
			for (Field f : fs) {
				if (f.getType().isAssignableFrom(fieldType))
					return f;
			}
		}
		if (throwIfNotfound)
			throw new ServiceException("can not find class[" + clazz
					+ "]'s fieldType [" + fieldType + "]");
		return null;
	}

	public static List<String> getPropertiesName(Object obj) {
		if (obj instanceof Map) {
			return new ArrayList<String>(((Map) obj).keySet());
		} else {
			return desribPropertiesName(getObjectClass(obj));
		}
	}

	public static List<String> desribPropertiesName(Class<?> clazz) {
		List list = new ArrayList();
		desribPropertiesName(clazz, list);
		return list;
	}
	
	public static Collection<String> desribPropertiesName(Class<?> clazz, Class<? extends Collection> collectionCls) {
		Collection<String> propNames = newCollections(collectionCls);
		desribPropertiesName(clazz, propNames);
		return propNames;
	}

	public static void desribPropertiesName(Class<?> clazz, Collection<String> propsName) {
		PropertyDescriptor[] props = desribProperties(clazz);
		if (props == null)
			return;
		for (PropertyDescriptor p : props) {
			propsName.add(p.getName());
		}
	}

	public static List<Class> findSuperClasses(Class clazz) {
		return findSuperClasses(clazz, Object.class);
	}

	public static List<Class> findSuperClasses(Class clazz, Class stopClass) {
		List<Class> classes = new ArrayList<Class>();
		Class parent = clazz.getSuperclass();
		while (parent != null && !parent.equals(stopClass)) {
			classes.add(parent);
			parent = parent.getSuperclass();
		}
		return classes;

	}

	public static Object invokeMethod(String methodName, Object target,
			Object... args) {
		return invokeMethod(findMethod(getObjectClass(target), methodName,
				findTypes(args)), target, args);
	}

	public static Class getObjectClass(Object target) {
		if(target==null)
			return null;
		Class targetClass = null;
		if (target instanceof Class)
			targetClass = (Class) target;
		else
			targetClass = target.getClass();
		return targetClass;
	}

	public static Class[] findTypes(Object... args) {
		Class[] argTypes = null;
		if (args != null) {
			for (Object arg : args) {
				if (arg == null)
					continue;
				Class t = arg.getClass();
				argTypes = (Class[]) ArrayUtils.add(argTypes, t);
			}
		}
		return argTypes;
	}

	public static Object invokeMethod(String methodName, Object target) {
		return invokeMethod(findMethod(target.getClass(), methodName,
				(Class[]) null), target, (Object[]) null);
	}

	public static Object invokeMethod(boolean throwIfError, Method method,
			Object target) {
		return invokeMethod(throwIfError, method, target, (Object[]) null);
	}

	public static Object invokeMethod(Method method, Object target) {
		return invokeMethod(method, target, (Object[]) null);
	}

	public static Object invokeMethod(Method method, Object target,
			Object... args) {
		return invokeMethod(true, method, target, args);
	}

	public static Object invokeMethod(boolean throwIfError, Method method,
			Object target, Object... args) {
		try {
			if(method==null)
				throw new NullPointerException("this method is not exist!");
			if (!method.isAccessible())
				method.setAccessible(true);
			return method.invoke(target, args);
		} catch (Exception ex) {
			if (throwIfError)
				throw new BaseException("invoke target["+target+"] method[" + method + "] error: " + ex.getMessage(), ex);
			else
				return null;
		}
	}

	public static void setFieldValue(String fieldName, Object obj, Object value) {
		setBean(obj, fieldName, value);
	}

	public static void setBean(Object obj, String fieldName, Object value) {
		Field field = findField(obj.getClass(), fieldName, true);
		setFieldValue(field, obj, value);
	}

	public static void setBean(Object obj, Class fieldType, Object value) {
		Field field = findField(obj.getClass(), fieldType, true);
		setFieldValue(field, obj, value);
	}

	public static void setFieldValue(Field f, Object obj, Object value) {
		setBean(obj, f, value);
	}

	public static void setBean(Object obj, Field f, Object value) {
		try {
			if (!f.isAccessible())
				f.setAccessible(true);
			f.set(obj, LangUtils.tryCastTo(value, f.getType()));
		} catch (Exception ex) {
			throw new ServiceException("invoke method error: "
					+ ex.getMessage(), ex);
		}
	}

	public static Object getFieldValue(Object obj, String fieldName) {
		if (obj instanceof Map) {
			return getValue((Map) obj, fieldName);
		}
		return getFieldValue(obj, fieldName, true);
	}

	public static Object getFieldValue(Object obj, String fieldName,
			boolean throwIfError) {
		Field f = findField(obj.getClass(), fieldName);
		return getFieldValue(f, obj, throwIfError);
	}

	public static Object getFieldValue(Field f, Object obj, boolean throwIfError) {
		try {
			if (!f.isAccessible())
				f.setAccessible(true);
			return f.get(obj);
		} catch (Exception ex) {
			if (throwIfError)
				throw new ServiceException("get value of field[" + f
						+ "] error: " + ex.getMessage(), ex);
			else
				return null;
		}
	}

	public static void setFieldValueBySetter(Object obj, String fieldName,
			Object value, boolean throwIfError) {
		Field f = findField(obj.getClass(), fieldName);
		setFieldValueBySetter(obj, f, value, throwIfError);
	}

	public static Object getFieldValueByGetter(Object obj, String fieldName,
			boolean throwIfError) {
		Field f = findField(obj.getClass(), fieldName);
		return getFieldValueByGetter(obj, f, throwIfError);
	}

	public static Object getFieldValueByGetter(Object obj, Field f,
			boolean throwIfError) {
		try {
			String getterName = "get"
					+ org.onetwo.common.utils.StringUtils.toJavaName(f
							.getName(), true);
			Method getter = findMethod(obj.getClass(), getterName);
			Object val = invokeMethod(getter, obj);
			return val;
		} catch (Exception ex) {
			if (throwIfError)
				throw new ServiceException("get value of field[" + f
						+ "] error: " + ex.getMessage(), ex);
			else
				return null;
		}
	}

	public static void setFieldValueBySetter(Object obj, Field f, Object value,
			boolean throwIfError) {
		try {
			String setterName = "set"
					+ org.onetwo.common.utils.StringUtils.toJavaName(f
							.getName(), true);
			Method setter = findMethod(obj.getClass(), setterName, f.getType());
			invokeMethod(setter, obj, value);
		} catch (Exception ex) {
			if (throwIfError)
				throw new ServiceException("get value of field[" + f
						+ "] error: " + ex.getMessage(), ex);
		}
	}

	public static Object getStaticFieldValue(Class clazz, String fieldName) {
		Field field = findField(clazz, fieldName, true);
		return getFieldValue(field, clazz, true);
	}

	public static boolean isEqualsMethod(Method method) {
		if (method == null || !method.getName().equals("equals")) {
			return false;
		}
		Class[] paramTypes = method.getParameterTypes();
		return (paramTypes.length == 1 && paramTypes[0] == Object.class);
	}

	public static boolean isHashCodeMethod(Method method) {
		return (method != null && method.getName().equals("hashCode") && method
				.getParameterTypes().length == 0);
	}

	public static boolean isToStringMethod(Method method) {
		return (method != null && method.getName().equals("toString") && method
				.getParameterTypes().length == 0);
	}

	private static Expression METHOD_EXP = ExpressionFacotry.newExpression("(", ")",
			new ValueProvider() {

				@Override
				public String findString(String var) {
					return "";
				}

			});

	public static boolean isMethodExp(String expr) {
		return METHOD_EXP.isExpresstion(expr);
//		return expr.endsWith("()");
	}
	

	public static Object getExpr(Object bean, String name) {
		return getExpr(bean, name, false);
	}

	/************
	 * 
	 * @param bean
	 * @param name this-property => thisProperty
	 * @param newIfNull
	 * @return
	 */
	public static Object getExpr(Object bean, String name, boolean newIfNull) {
		Assert.notNull(bean);
		Assert.hasText(name);
		Object parentObj = bean;
		String[] paths = StringUtils.split(name, ".");
		// String targetProperty = name;
//		int index = 0;
		Object pathObj = null;
		for (String path : paths) {
			
			if (path.indexOf('-') != -1) {
				path = org.onetwo.common.utils.StringUtils.toJavaName(path, '-', false);
			}
			if (parentObj == null)
				LangUtils.throwBaseException("expr[" + name + "] parent object can not be null : " + path);
			
			if (isMethodExp(path)) {
				path = METHOD_EXP.parse(path);
				pathObj = invokeMethod(path, parentObj);
			} else {
				if(parentObj instanceof Map){
					pathObj = getValue((Map)parentObj, path);
				}else{
					PropertyDescriptor pd = getPropertyDescriptor(parentObj, path);
					if(pd!=null){// property first
						pathObj = getProperty(parentObj, pd);
						if(pathObj==null && newIfNull){
							pathObj = newInstance(pd.getPropertyType());
							setProperty(parentObj, pd, pathObj);
						}
					}else{
						Field f = findField(parentObj.getClass(), path);
						if(f!=null){
							pathObj = getFieldValue(f, parentObj, true);
							if(pathObj==null && newIfNull){
								pathObj = newInstance(f.getType());
								setFieldValue(f, parentObj, pathObj);
							}
						}else{
//							pathObj = invokeMethod(path, parentObj);
						}
					}
				}
			}
			parentObj = pathObj;
		}
		return pathObj;
	}
	

	public static void setExpr(Object bean, String name, Object value) {
		setExpr(bean, name, value, false);
	}

	public static void setExpr(Object bean, String name, Object value, boolean tryMethod) {
		Assert.notNull(bean, "bean can not be null");
		Assert.hasText(name, "name must has text");
		int lastDot = name.lastIndexOf('.');
		String setPropertyName = "";
		Object parentObj = bean;
		if(lastDot==-1){
			setPropertyName = name;
		}else{
			String targetObjPath = "";
			setPropertyName = name.substring(lastDot+1);
			targetObjPath = name.substring(0, lastDot);
			parentObj = getExpr(bean, targetObjPath, true);
		}
		if(parentObj instanceof Map){
			((Map)parentObj).put(setPropertyName, value);
		}else{
			PropertyDescriptor pd = getPropertyDescriptor(parentObj, setPropertyName);
			if(pd!=null){// property first
				setProperty(parentObj, pd, value);
			}else{
				Field f = findField(parentObj.getClass(), setPropertyName, false);
				if(f!=null){
					setFieldValue(f, parentObj, value);
				}else{
					if(tryMethod && value!=null){
						Method mehtod = findMethod(true, getObjectClass(parentObj), setPropertyName, findTypes(value));
						if(mehtod!=null)
							invokeMethod(setPropertyName, parentObj, value);
						else
							LangUtils.throwBaseException("set ["+bean.getClass().getSimpleName() +": "+bean+"] 's value error by expr : " + name);
					}
				}
			}
		}
	}

	public static void setStringDefaultValue(Object inst, String val) {
		setFieldsDefaultValue(inst, String.class, val);
	}

	public static void setFieldsDefaultValue(Object inst, Object... objects) {
		Map properties = M.c(objects);
		setFieldsDefaultValue(inst, properties);
	}

	/********
	 * 根据map的设置来设置默认值
	 * @param inst
	 * @param properties
	 */
	public static void setFieldsDefaultValue(Object inst, Map properties) {
		Collection<Field> fields = ReflectUtils.findFieldsFilterStatic(inst
				.getClass());
		if (fields == null || fields.isEmpty())
			return;
		// Map properties = M.c(objects);
		for (Field f : fields) {
			if (properties.containsKey(f.getName())) {
				ReflectUtils.setFieldValue(f, inst, processValue(f, properties.get(f.getName())));
				continue;
			}
			if (properties.containsKey(f.getType())) {
				ReflectUtils.setFieldValue(f, inst, processValue(f, properties.get(f.getType())));
				continue;
			}
		}
	}

	/*****
	 * 如果是:name，返回字段名称，否则返回传入的值；
	 * @param field
	 * @param val
	 * @return
	 */
	protected static Object processValue(Field field, Object val) {
		if (!(val instanceof String))
			return val;
		Object newVal = val;
		if (val.toString().equals(":name")) {
			newVal = field.getName();
		}
		return newVal;
	}


	public static <T> List<T> times(int count, Object delegate, String method, Object... args) {
		return times(delegate, method, count, false, args);
	}
	
	public static <T> List<T> times(Object delegate, String method, int count, boolean passCountByParams, Object... args) {
		DelegateMethod d = DelegateFactory.create(delegate, method);
		
		List<T> results = new ArrayList<T>();

		Object[] pass = null;
		for (int i = 0; i < count; i++) {
			if (passCountByParams) {
				pass = ArrayUtils.add(args, 0, i);
			} else {
				pass = args;
			}
			T result = (T) d.invoke(pass);
			results.add(result);
		}
		return results;
	}

	public static <T> List<T> newInstance(Class<T> clazz, int count,
			ListFun<T> closure) {
		List<T> results = new ArrayList<T>();
		for (int i = 0; i < count; i++) {
			T result = ReflectUtils.newInstance(clazz);
			if (closure != null)
				closure.exe(result, i, null);
			results.add(result);
		}
		return results;
	}
	

	public static PropertyDescriptor newProperty(Class clazz, String propName){
		try {
			return new PropertyDescriptor(propName, clazz);
		} catch (IntrospectionException e) {
			LangUtils.throwBaseException("newProperty error : " + e.getMessage(), e);
		}
		return null;
	}

	public static PropertyDescriptor newProperty(String name, Method rMethod, Method wMethod){
		try {
			return new PropertyDescriptor(name, rMethod, wMethod);
		} catch (IntrospectionException e) {
			LangUtils.throwBaseException("create property["+name+"] error : " + e.getMessage(), e);
		}
		return null;
	}

	public static PropertyDescriptor newProperty(Class<?> entityClass, Field field){
		try {
			Method rMethod = ReflectUtils.getReadMethod(entityClass, field.getName(), field.getType());
			Method wMethod = ReflectUtils.getWriteMethod(entityClass, field.getName());
			return new PropertyDescriptor(field.getName(), rMethod, wMethod);
		} catch (IntrospectionException e) {
			LangUtils.throwBaseException("newProperty["+entityClass+"."+field.getName()+"] error : " + e.getMessage(), e);
		}
		return null;
	}
	
	public static Class<?>[] getObjectClasses(Object[] objs){
		if(LangUtils.isEmpty(objs))
			return null;
		List<Class<?>> clslist = new ArrayList<Class<?>>(objs.length);
		for(Object obj : objs){
			clslist.add(obj.getClass());
		}
		return clslist.toArray(new Class[clslist.size()]);
	}
	
	/*****
	 * 
	 * @param srcClass
	 * @param destClass
	 * @param separator src单词之间的分隔符
	 * @return
	 */
	public static Map<String, String> mappedFields(Class<?> srcClass, Class<?> destClass, char separator){
		Collection<String> srcFields = ReflectUtils.findInstanceFieldNames(srcClass, Set.class);
		Collection<String> desctFields = ReflectUtils.findInstanceFieldNames(destClass, Set.class);
//		Collection<String> mapFields = srcFields.size()<desctFields.size()?srcFields:desctFields;
		Map<String, String> mappingFields = LangUtils.newHashMap();
//		String s = String.valueOf(separator);
		for(String fname : srcFields){
			if(desctFields.contains(fname)){
				mappingFields.put(fname, fname);
				
			}else if(fname.indexOf(separator)!=-1){
				String destname = StringUtils.toJavaName(fname, separator, false);
				if(desctFields.contains(destname)){
					mappingFields.put(fname, destname);
				}
			}
			/*else if(StringUtils.hasUpper(fname)){
				String destname = StringUtils.convert2UnderLineName(fname, s);
				if(desctFields.contains(destname)){
					mappingFields.put(fname, destname);
				}
			}*/
		}
		
		return mappingFields;
	}

	public static void main(String[] args) {

	}

}
