package org.onetwo.common.utils;

import java.net.URL;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.list.JFishList;

final public class ClassUtils {
	
	public static final String NON_PRIMITIVE_ARRAY_PREFIX = "[L";

	public static final char PACKAGE_SEPARATOR = '.';
	
	public static final char INNER_CLASS_SEPARATOR = '$';

	public static final String CGLIB_CLASS_SEPARATOR = "$$";

	public static final String CLASS_FILE_SUFFIX = ".class";

	private ClassUtils(){
	}
	

	private static Class<?> forName(String name, ClassLoader classLoader)
			throws ClassNotFoundException, LinkageError {

		Assert.notNull(name, "Name must not be null");

		ClassLoader clToUse = classLoader;
		if (clToUse == null) {
			clToUse = getDefaultClassLoader();
		}
		try {
			return Class.forName(name, false, clToUse);
		}
		catch (ClassNotFoundException ex) {
			int lastDotIndex = name.lastIndexOf(PACKAGE_SEPARATOR);
			if (lastDotIndex != -1) {
				String innerClassName =
						name.substring(0, lastDotIndex) + INNER_CLASS_SEPARATOR + name.substring(lastDotIndex + 1);
				try {
					return Class.forName(innerClassName, false, clToUse);
				}
				catch (ClassNotFoundException ex2) {
					// Swallow - let original exception get through
				}
			}
			throw ex;
		}
	}

	/***
	 * 复制自spring ClassUtils，去掉了部分复杂的判断，只是一个简单的判断。复杂场景不建议使用此方法。
	 * @param className
	 * @param classLoader
	 * @return
	 */
	public static boolean isPresent(String className, ClassLoader classLoader) {
		try {
			forName(className, classLoader);
			return true;
		}
		catch (IllegalAccessError err) {
			throw new IllegalStateException("Readability mismatch in inheritance hierarchy of class [" +
					className + "]: " + err.getMessage(), err);
		}
		catch (Throwable ex) {
			// Typically ClassNotFoundException or NoClassDefFoundError...
			return false;
		}
	}
	
	public static String getCleanedClassName(String innerClassName) {
		return innerClassName.replaceAll("\\$", ".");
	}

	public static ClassLoader getDefaultClassLoader(){
		ClassLoader cld = null;
		try {
			cld = Thread.currentThread().getContextClassLoader();
		} catch (Exception e) {
			//ignore
		}
		if(cld==null){
			cld = ClassUtils.class.getClassLoader();
		}
		return cld;
	}
	

	public static JFishList<URL> getResources(String resourceName) {
		return getResources(resourceName, ClassUtils.class, false);
	}
	
	public static JFishList<URL> getResources(String resourceName, Class<?> callingClass, boolean aggregate) {

		JFishList<URL> list = new JFishList<URL>();

//        list.addEnumeration(Thread.currentThread().getContextClassLoader().getResources(resourceName));
		try {
			list.addEnumeration(getDefaultClassLoader().getResources(resourceName));

	        if (!list.isNotEmpty() || aggregate) {
	            list.addEnumeration(ClassUtils.class.getClassLoader().getResources(resourceName));
	        }

	        if (!list.isNotEmpty() || aggregate) {
	            ClassLoader cl = callingClass.getClassLoader();

	            if (cl != null) {
	                list.addEnumeration(cl.getResources(resourceName));
	            }
	        }
		} catch (Exception e) {
			throw new BaseException("getResources error : " + resourceName);
		}

        if (!list.isNotEmpty() && (resourceName != null) && ((resourceName.length() == 0) || (resourceName.charAt(0) != '/'))) { 
            return getResources('/' + resourceName, callingClass, aggregate);
        }

        return list;
    }
}
