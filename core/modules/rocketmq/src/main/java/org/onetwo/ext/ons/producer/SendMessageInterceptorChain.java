package org.onetwo.ext.ons.producer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Supplier;

import org.onetwo.common.annotation.AnnotationUtils;
import org.onetwo.common.db.dquery.DynamicMethod;
import org.onetwo.common.db.dquery.MethodDynamicQueryInvokeContext;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.dbm.annotation.DbmInterceptorFilter.InterceptorType;
import org.onetwo.dbm.annotation.DbmJdbcOperationMark;
import org.onetwo.dbm.exception.DbmException;
import org.onetwo.dbm.jdbc.spi.DbmInterceptor;
import org.onetwo.dbm.jdbc.spi.DbmJdbcOperationType;
import org.onetwo.dbm.jdbc.spi.DbmJdbcOperationType.DatabaseOperationType;
import org.springframework.core.NestedRuntimeException;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

abstract public class SendMessageInterceptorChain {

	final private Supplier<Object> actualInvoker;
	
	private LinkedList<SendMessageInterceptor> interceptors;
	private Iterator<SendMessageInterceptor> iterator;
	private Object result;
	private Throwable throwable;

	public SendMessageInterceptorChain(Supplier<Object> actualInvoker, LinkedList<SendMessageInterceptor> interceptors) {
		super();
		this.actualInvoker = actualInvoker;
		this.interceptors = interceptors;
		this.iterator = this.interceptors.iterator();
	}

	public SendMessageInterceptorChain addInterceptorToHead(SendMessageInterceptor...interceptors){
		if(LangUtils.isEmpty(interceptors)){
			return this;
		}
		this.interceptors.addAll(0, Arrays.asList(interceptors));
		return this;
	}
	
	public SendMessageInterceptorChain addInterceptorToTail(SendMessageInterceptor...interceptors){
		if(LangUtils.isEmpty(interceptors)){
			return this;
		}
		for (int i = 0; i < interceptors.length; i++) {
			this.interceptors.addLast(interceptors[i]);
		}
		return this;
	}
	

	public Object invoke(){
		if(iterator.hasNext()){
			DbmInterceptor interceptor = iterator.next();
			result = interceptor.intercept(this);
		}else{
			if(actualInvoker!=null){
				result = actualInvoker.get();
			}else{
				if (!targetMethod.isAccessible()){
					targetMethod.setAccessible(true);
				}
				try {
					result = targetMethod.invoke(targetObject, targetArgs);
					state = STATE_FINISH;
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					state = STATE_EXCEPTION;
					throwable = e;
					throw convertRuntimeException(e);
				}
			}
		}
		return result;
	}
	
	private RuntimeException convertRuntimeException(Exception e){
		if(e instanceof InvocationTargetException){
			InvocationTargetException ite = (InvocationTargetException)e;
			if(ite.getTargetException() instanceof NestedRuntimeException){
				return (NestedRuntimeException)ite.getTargetException();
			}
		}
		return new DbmException("invoke method error: " + targetMethod, e);
	}

	@Override
	public Object getResult() {
		return result;
	}
	
	@Override
	public Throwable getThrowable() {
		return throwable;
	}

	public static class JdbcDbmInterceptorChain extends SendMessageInterceptorChain {
		private Optional<DbmJdbcOperationType> dbmJdbcOperationType;

		public JdbcDbmInterceptorChain(Object targetObject, Method targetMethod, Object[] targetArgs, Collection<DbmInterceptor> interceptors) {
			super(targetObject, targetMethod, targetArgs, interceptors);
			this.setType(InterceptorType.JDBC);
		}

		@Override
		public Optional<DbmJdbcOperationType> getJdbcOperationType() {
			if(dbmJdbcOperationType==null){
				DbmJdbcOperationMark operation = AnnotationUtils.findAnnotationWithStopClass(getTargetObject().getClass(), getTargetMethod(), DbmJdbcOperationMark.class);
				Optional<DbmJdbcOperationType> dbmJdbcOperationType = operation==null?Optional.empty():Optional.ofNullable(operation.type());
				this.dbmJdbcOperationType = dbmJdbcOperationType;
				return dbmJdbcOperationType;
			}
			return dbmJdbcOperationType;
		}

		@Override
		public Optional<DatabaseOperationType> getDatabaseOperationType() {
			Optional<DbmJdbcOperationType> jdbcType = this.getJdbcOperationType();
			return jdbcType.isPresent()?Optional.of(jdbcType.get().getDatabaseOperationType()):Optional.empty();
		}
	}
	
	public static class SessionDbmInterceptorChain extends JdbcDbmInterceptorChain {

		public SessionDbmInterceptorChain(Object targetObject, Method targetMethod, Object[] targetArgs, Collection<DbmInterceptor> interceptors) {
			super(targetObject, targetMethod, targetArgs, interceptors);
			this.setType(InterceptorType.SESSION);
		}
		
	}
	
	
	public static class RepositoryDbmInterceptorChain extends SendMessageInterceptorChain {
		final private MethodDynamicQueryInvokeContext invokeContext;

		public RepositoryDbmInterceptorChain(Object targetObject, MethodDynamicQueryInvokeContext invokeContext, Collection<DbmInterceptor> interceptors, Supplier<Object> actualInvoker) {
			super(targetObject, invokeContext.getDynamicMethod().getMethod(), invokeContext.getParameterValues(), interceptors, actualInvoker);
			this.invokeContext = invokeContext;
			this.setType(InterceptorType.REPOSITORY);
		}

		@Override
		public Optional<DbmJdbcOperationType> getJdbcOperationType() {
			return Optional.empty();
		}

		@Override
		public Optional<DatabaseOperationType> getDatabaseOperationType() {
			DatabaseOperationType type = null;
			DynamicMethod dynamicMethod = invokeContext.getDynamicMethod();
			if(dynamicMethod.isBatch()){
				type = DatabaseOperationType.BATCH;
			}else if(dynamicMethod.isExecuteUpdate()){
				type = DatabaseOperationType.UPDATE;
			}else{
				type = DatabaseOperationType.QUERY;
			}
			return Optional.<DbmJdbcOperationType.DatabaseOperationType>of(type);
		}

	}

}
