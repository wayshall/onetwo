package org.onetwo.dbm.core.spi;

import java.lang.reflect.Method;

import org.onetwo.dbm.annotation.DbmInterceptorFilter.InterceptorType;
import org.onetwo.dbm.core.internal.DefaultDbmInterceptorChain;

public interface DbmInterceptorChain {
	
	InterceptorType getType();
	
	Object invoke();

	Object getResult();


	Object getTargetObject();

	Method getTargetMethod();

	Object[] getTargetArgs();
	
	boolean isDatabaseUpdate();
	boolean isDatabaseRead();

	DefaultDbmInterceptorChain addInterceptorToHead(DbmInterceptor...interceptors);
	
	DefaultDbmInterceptorChain addInterceptorToTail(DbmInterceptor...interceptors);
	
	DefaultDbmInterceptorChain addInterceptor(DbmInterceptor...interceptors);
}