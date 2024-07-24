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
	
	public static String getCleanedClassName(String innerClassName) {
		return innerClassName.replaceAll("\\$", ".");
	}


	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		}
		catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = ClassUtils.class.getClassLoader();
			if (cl == null) {
				// getClassLoader() returning null indicates the bootstrap ClassLoader
				try {
					cl = ClassLoader.getSystemClassLoader();
				}
				catch (Throwable ex) {
					// Cannot access system ClassLoader - oh well, maybe the caller can live with null...
				}
			}
		}
		return cl;
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
