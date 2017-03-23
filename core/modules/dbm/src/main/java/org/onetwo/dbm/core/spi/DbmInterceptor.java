package org.onetwo.dbm.core.spi;

public interface DbmInterceptor {
	
	Object intercept(DbmInterceptorChain chain);

}
