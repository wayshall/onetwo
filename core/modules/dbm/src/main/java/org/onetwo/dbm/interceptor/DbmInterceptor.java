package org.onetwo.dbm.interceptor;

public interface DbmInterceptor {
	
	Object intercept(DbmInterceptorChain chain);

}
