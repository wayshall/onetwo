package org.onetwo.ext.poi.utils;

import java.util.Map;
import java.util.WeakHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

public class ClassIntroManager {
	
	private static final Logger logger = LoggerFactory.getLogger(ClassIntroManager.class);

	private static final ClassIntroManager introManager = new ClassIntroManager();
	
	
	public static ClassIntroManager getInstance() {
		return introManager;
	}

	private Map<Class<?>, Intro<?>> introMaps = new WeakHashMap<Class<?>, Intro<?>>(500);
	private Object lock = new Object();

	@SuppressWarnings("unchecked")
	public <T> Intro<T> getIntro(Class<T> clazz){
		if(clazz==null)
			return null;
		Intro<T> intro = (Intro<T>)introMaps.get(clazz);
		if(intro==null){
			synchronized (lock) {
				intro = (Intro<T>)introMaps.get(clazz);
				if(intro==null){
					intro = Intro.wrap(clazz);
					introMaps.put(clazz, intro);
				}
			}
		}
		return intro;
	}

	public Intro<?> getIntro(String className){
		if(ExcelUtils.isBlank(className))
			return null;
		Class<?> clazz = loadClass(className);
		return getIntro(clazz);
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
				throw new RuntimeException("class not found : " + className, e);
			else
				logger.warn("class not found : " + className);
		}
		return clz;
	}
}
