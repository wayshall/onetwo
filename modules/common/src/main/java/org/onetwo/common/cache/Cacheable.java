package org.onetwo.common.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {

	String key() default "";
	String group() default CacheUtils.DEFAULT_CACHE_GROUP;
	int expire() default -1;//seconds
	boolean useKeyHashCode() default false;//do not use hashcode option if need to flush cache by group, because the hashcode will generated with method
	
}
