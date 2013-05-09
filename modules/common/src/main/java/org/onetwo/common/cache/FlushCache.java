package org.onetwo.common.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FlushCache {

	String key() default "";
	String[] group() default CacheUtils.DEFAULT_CACHE_GROUP;
//	boolean useKeyHashCode() default false;//usual don't use
//	boolean flushAllOfGroup() default false;
}
