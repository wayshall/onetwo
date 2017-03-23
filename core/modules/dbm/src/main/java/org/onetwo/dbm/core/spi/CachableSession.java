package org.onetwo.dbm.core.spi;


public interface CachableSession extends DbmSession {

	void putCacche(Object key, Object value);

	Object getCaccheOrInvoke(DbmInterceptorChain chain);

}