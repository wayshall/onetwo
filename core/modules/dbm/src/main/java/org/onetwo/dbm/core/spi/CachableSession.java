package org.onetwo.dbm.core.spi;

import java.lang.reflect.Method;

public interface CachableSession {

	void putCacche(Object key, Object value);

	Object getCacche(Object key);

	Object generateCacheKey(Object target, Method method, Object... params);

}