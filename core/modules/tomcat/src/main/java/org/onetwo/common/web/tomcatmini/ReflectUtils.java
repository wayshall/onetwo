package org.onetwo.common.web.tomcatmini;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final public class ReflectUtils {

    private static final Logger logger = LoggerFactory.getLogger(ReflectUtils.class);
	
	private ReflectUtils(){
	}
	

	public static Class<?> loadClass(String className, boolean throwIfError) {
		Class<?> clz = null;
		try {
//			clz = Class.forName(className);
			clz = Class.forName(className, true, getDefaultClassLoader());
		} catch (Exception e) {
			if(throwIfError)
				throw new RuntimeException("class not found : " + className, e);
			else
				logger.warn("class not found : " + className);
		}
		return clz;
	}
	
	public static ClassLoader getDefaultClassLoader(){
		ClassLoader cld = null;
		try {
			cld = Thread.currentThread().getContextClassLoader();
		} catch (Exception e) {
			//ignore
		}
		if(cld==null){
			cld = ReflectUtils.class.getClassLoader();
		}
		return cld;
	}

	public static Object invokeMethod(Method method, Object target, Object... args) {
		return invokeMethod(true, method, target, args);
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

	public static Object invokeMethod(String methodName, Object target, Object... args) {
		Method m = findMethod(true, getObjectClass(target), methodName, findTypes(args));
		return invokeMethod(m, target, args);
	}
	public static Class<?>[] findTypes(Object... args) {
		List<Class<?>> argTypes = new ArrayList<Class<?>>();
		if (args != null) {
			for (Object arg : args) {
				if (arg == null)
					continue;
				Class<?> t = arg.getClass();
				argTypes.add(t);
			}
		}
		return argTypes.toArray(new Class<?>[0]);
	}
	public static Class<?> getObjectClass(Object target) {
		if(target==null)
			return null;
		Class<?> targetClass = null;
		if (target instanceof Class)
			targetClass = (Class<?>) target;
		else
			targetClass = target.getClass();
		return targetClass;
	}
	
	public static Method findMethod(boolean ignoreIfNotfound, Class<?> objClass, String name, Class<?>... paramTypes) {
		try {
			Class<?> searchType = objClass;
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
					if (!method.isBridge() && name.equals(method.getName()) && (isEmpty(paramTypes) || matchParameterTypes(paramTypes, method.getParameterTypes()))) {
						// System.out.println("====>>>method match");
						return method;
					}
				}
				searchType = searchType.getSuperclass();
			}
		} catch (Exception e) {
			if (ignoreIfNotfound)
				return null;
			throw new RuntimeException("can not find the method : [class=" + objClass + ", methodName=" + name + ", paramTypes=" + paramTypes + "]");
		}
		if (ignoreIfNotfound)
			return null;
		throw new RuntimeException("can not find the method : [class=" + objClass + ", methodName=" + name + ", paramTypes=" + paramTypes + "]");
	}

	public static boolean isEmpty(Object[] arrays){
		return (arrays==null || arrays.length==0);
	}

	public static boolean matchParameterTypes(Class<?>[] sourceTypes, Class<?>[] targetTypes) {
		if (sourceTypes.length != targetTypes.length)
			return false;
		int index = 0;
		for (Class<?> cls : targetTypes) {
			if (!cls.isAssignableFrom(sourceTypes[index]))
				return false;
			index++;
		}
		return true;
	}

}
