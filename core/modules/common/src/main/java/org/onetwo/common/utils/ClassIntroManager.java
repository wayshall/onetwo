package org.onetwo.common.utils;

import java.util.Map;
import java.util.WeakHashMap;

public class ClassIntroManager {
	
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
		if(StringUtils.isBlank(className))
			return null;
		Class<?> clazz = ReflectUtils.loadClass(className);
		return getIntro(clazz);
	}

	
}
