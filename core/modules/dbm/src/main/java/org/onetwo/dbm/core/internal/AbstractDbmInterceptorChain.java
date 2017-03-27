package org.onetwo.dbm.core.internal;

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
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.dbm.annotation.DbmInterceptorFilter.InterceptorType;
import org.onetwo.dbm.annotation.DbmJdbcOperationMark;
import org.onetwo.dbm.core.DbmJdbcOperationType;
import org.onetwo.dbm.core.DbmJdbcOperationType.DatabaseOperationType;
import org.onetwo.dbm.core.spi.DbmInterceptor;
import org.onetwo.dbm.core.spi.DbmInterceptorChain;
import org.springframework.core.NestedRuntimeException;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

abstract public class AbstractDbmInterceptorChain implements DbmInterceptorChain {
	
	public static final int STATE_INIT = 0;
	public static final int STATE_EXECUTING = 1;
	public static final int STATE_FINISH = 2;
	public static final int STATE_EXCEPTION = -1;

	final private Object targetObject;
	final private Method targetMethod;
	final private Object[] targetArgs;

	final private Supplier<Object> actualInvoker;
	
	private LinkedList<DbmInterceptor> interceptors;
	private Iterator<DbmInterceptor> iterator;
	private Object result;
	private Throwable throwable;
	private int state = STATE_INIT;
	
	private InterceptorType type;

	public AbstractDbmInterceptorChain(Object targetObject, Method targetMethod,
			Object[] targetArgs, Collection<DbmInterceptor> interceptors) {
		this(targetObject, targetMethod, targetArgs, interceptors, null);
	}
	public AbstractDbmInterceptorChain(Object targetObject, Method targetMethod,
			Object[] targetArgs, Collection<DbmInterceptor> interceptors, Supplier<Object> actualInvoker) {
		super();
		this.targetObject = targetObject;
		this.targetMethod = targetMethod;
		this.targetArgs = targetArgs;
		this.interceptors = new LinkedList<DbmInterceptor>(interceptors);
		this.actualInvoker = actualInvoker;
	}
	
	protected void setType(InterceptorType type) {
		this.type = type;
	}
	@Override
	public InterceptorType getType() {
		return type;
	}
	private void checkState(String msg){
		if(this.state!=STATE_INIT){
			throw new IllegalStateException("illegal state for: "+msg);
		}
	}

	public Object getTargetObject() {
		return targetObject;
	}

	public Method getTargetMethod() {
		return targetMethod;
	}

	public Object[] getTargetArgs() {
		return targetArgs;
	}

	public AbstractDbmInterceptorChain addInterceptorToHead(DbmInterceptor...interceptors){
		this.checkState("addInterceptorToHead");
		if(LangUtils.isEmpty(interceptors)){
			return this;
		}
		this.interceptors.addAll(0, Arrays.asList(interceptors));
		return this;
	}
	
	public AbstractDbmInterceptorChain addInterceptorToTail(DbmInterceptor...interceptors){
		this.checkState("addInterceptorToTail");
		if(LangUtils.isEmpty(interceptors)){
			return this;
		}
		for (int i = 0; i < interceptors.length; i++) {
			this.interceptors.addLast(interceptors[i]);
		}
		return this;
	}
	
	/****
	 * 调用此方法后会重新排序，因此addInterceptorToHead和addInterceptorToTail会失效
	 */
	public AbstractDbmInterceptorChain addInterceptor(DbmInterceptor...interceptors){
		this.checkState("addInterceptor");
		if(LangUtils.isEmpty(interceptors)){
			return this;
		}
		this.interceptors.addAll(Arrays.asList(interceptors));
		AnnotationAwareOrderComparator.sort(this.interceptors);
		return this;
	}
	

	@Override
	public Object invoke(){
		if(state==STATE_INIT){
			state = STATE_EXECUTING;
			this.iterator = this.interceptors.iterator();
		}
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
		return new BaseException("invoke method error: " + targetMethod);
	}

	@Override
	public Object getResult() {
		return result;
	}
	
	@Override
	public Throwable getThrowable() {
		return throwable;
	}

	public static class JdbcDbmInterceptorChain extends AbstractDbmInterceptorChain {
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
	
	
	public static class RepositoryDbmInterceptorChain extends AbstractDbmInterceptorChain {
		private DynamicMethod dynamicMethod;

		public RepositoryDbmInterceptorChain(Object targetObject, DynamicMethod dynamicMethod, Object[] targetArgs, Collection<DbmInterceptor> interceptors, Supplier<Object> actualInvoker) {
			super(targetObject, dynamicMethod.getMethod(), targetArgs, interceptors, actualInvoker);
			this.dynamicMethod = dynamicMethod;
			this.setType(InterceptorType.REPOSITORY);
		}

		@Override
		public Optional<DbmJdbcOperationType> getJdbcOperationType() {
			return Optional.empty();
		}

		@Override
		public Optional<DatabaseOperationType> getDatabaseOperationType() {
			DatabaseOperationType type = null;
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
