package org.onetwo.dbm.interceptor;

import java.lang.reflect.Method;

import org.onetwo.dbm.interceptor.annotation.DbmInterceptorFilter.InterceptorType;

public interface DbmInterceptorChain {
	
	InterceptorType getType();
	
	Object invoke();

	Object getResult();


	Object getTargetObject();

	Method getTargetMethod();

	Object[] getTargetArgs();

	DefaultDbmInterceptorChain addInterceptorToHead(DbmInterceptor...interceptors);
	
	DefaultDbmInterceptorChain addInterceptorToTail(DbmInterceptor...interceptors);
	
	DefaultDbmInterceptorChain addInterceptor(DbmInterceptor...interceptors);
}