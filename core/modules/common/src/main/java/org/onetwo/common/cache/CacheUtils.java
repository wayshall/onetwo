package org.onetwo.common.cache;

import java.lang.reflect.Method;

import org.onetwo.common.annotation.AnnotationUtils;

@SuppressWarnings("rawtypes")
public abstract class CacheUtils {
	
	public static final String DEFAULT_CACHE_GROUP = "default";
	
	public static boolean isCacheable(Class clazz){
		return AnnotationUtils.findAnnotation(clazz, Cacheable.class)!=null;
	}
	
	public static boolean isCacheable(Method method){
		return AnnotationUtils.findAnnotation(method, Cacheable.class)!=null;
	}
	
	public static Cacheable findCacheable(Class clazz, Method method){
		Cacheable cacheable = AnnotationUtils.findAnnotation(clazz, Cacheable.class);
		if(cacheable==null)
			cacheable = AnnotationUtils.findAnnotation(method, Cacheable.class);
		return cacheable;
	}
	
	public static FlushCache findFlushCache(Class clazz, Method method){
		FlushCache flashable = AnnotationUtils.findAnnotation(clazz, FlushCache.class);
		if(flashable==null)
			flashable = AnnotationUtils.findAnnotation(method, FlushCache.class);
		return flashable;
	}

}
