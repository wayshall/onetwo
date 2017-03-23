package org.onetwo.dbm.core.spi;

import java.lang.reflect.Method;
import java.util.Optional;

import org.onetwo.dbm.annotation.DbmInterceptorFilter.InterceptorType;
import org.onetwo.dbm.core.DbmJdbcOperationType;
import org.onetwo.dbm.core.DbmJdbcOperationType.DatabaseOperationType;
import org.onetwo.dbm.core.internal.AbstractDbmInterceptorChain;

public interface DbmInterceptorChain {
	
	InterceptorType getType();
	
	Object invoke();

	Object getResult();


	Object getTargetObject();

	Method getTargetMethod();

	Object[] getTargetArgs();

	Optional<DbmJdbcOperationType> getJdbcOperationType();
	Optional<DatabaseOperationType> getDatabaseOperationType();

	AbstractDbmInterceptorChain addInterceptorToHead(DbmInterceptor...interceptors);
	
	AbstractDbmInterceptorChain addInterceptorToTail(DbmInterceptor...interceptors);
	
	AbstractDbmInterceptorChain addInterceptor(DbmInterceptor...interceptors);
}