package org.onetwo.common.interceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;
import org.springframework.core.NestedRuntimeException;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

public class SimpleInterceptorChain<T extends Interceptor> implements InterceptorChain {
	
	public static interface ActualInvoker {
		Object invoke() throws Throwable;
	}
	
	public static final int STATE_INIT = 0;
	public static final int STATE_EXECUTING = 1;
	public static final int STATE_FINISH = 2;
	public static final int STATE_EXCEPTION = -1;

	final private Object targetObject;
	final private Method targetMethod;
	final private Object[] targetArgs;

	final private ActualInvoker actualInvoker;
	
	private LinkedList<T> interceptors;
	private Iterator<T> iterator;
	private Object result;
	private Throwable throwable;
	private int state = STATE_INIT;
	

	public SimpleInterceptorChain(Object targetObject, Method targetMethod,
			Object[] targetArgs, Collection<T> interceptors) {
		this(targetObject, targetMethod, targetArgs, interceptors, null);
	}
	
	public SimpleInterceptorChain(Object targetObject, Method targetMethod,
			Object[] targetArgs, Collection<T> interceptors, ActualInvoker actualInvoker) {
		super();
		this.targetObject = targetObject;
		this.targetMethod = targetMethod;
		this.targetArgs = targetArgs;
		this.interceptors = new LinkedList<>(interceptors);
		if (actualInvoker==null) {
			this.actualInvoker = () -> {
				if (!targetMethod.isAccessible()){
					targetMethod.setAccessible(true);
				}
				return targetMethod.invoke(targetObject, targetArgs);
			};
		} else {
			this.actualInvoker = actualInvoker;
		}
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

	@SuppressWarnings("unchecked")
	public SimpleInterceptorChain<T> addInterceptorToHead(T...interceptors){
		this.checkState("addInterceptorToHead");
		if(LangUtils.isEmpty(interceptors)){
			return this;
		}
		this.interceptors.addAll(0, Arrays.asList(interceptors));
		return this;
	}

	@SuppressWarnings("unchecked")
	public SimpleInterceptorChain<T> addInterceptorToTail(T...interceptors){
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
	@SuppressWarnings("unchecked")
	public SimpleInterceptorChain<T> addInterceptor(T...interceptors){
		this.checkState("addInterceptor");
		if(LangUtils.isEmpty(interceptors)){
			return this;
		}
		this.interceptors.addAll(Arrays.asList(interceptors));
		AnnotationAwareOrderComparator.sort(this.interceptors);
		return this;
	}
	

	public Object invoke() {
		if(state==STATE_INIT){
			state = STATE_EXECUTING;
			this.iterator = this.interceptors.iterator();
		}
		if(iterator.hasNext()){
			T interceptor = iterator.next();
			result = interceptor.intercept(this);
		}else{
			/*if(actualInvoker!=null){
				result = actualInvoker.invoke();
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
			}*/
			try {
				result = this.actualInvoker.invoke();
				state = STATE_FINISH;
			} catch (Throwable e) { 
				// IllegalAccessException | IllegalArgumentException | InvocationTargetException e
				state = STATE_EXCEPTION;
				throwable = e;
				throw convertRuntimeException(e);
			}
		}
		return result;
	}
	
	private RuntimeException convertRuntimeException(Throwable e){
		if (e instanceof InvocationTargetException) {
			InvocationTargetException ite = (InvocationTargetException)e;
			Throwable target = ite.getTargetException();
			if(target instanceof NestedRuntimeException){
				throw (NestedRuntimeException)ite.getTargetException();
			}
		} else if ( e instanceof RuntimeException) {
			throw (RuntimeException) e;
		}
		
		return new BaseException("invoke method error, targetMethod: " + targetMethod, e);
	}

	public Object getResult() {
		return result;
	}
	
	public Throwable getThrowable() {
		return throwable;
	}

}
