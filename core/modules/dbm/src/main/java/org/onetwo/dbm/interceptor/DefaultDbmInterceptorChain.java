package org.onetwo.dbm.interceptor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Supplier;

import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.dbm.interceptor.annotation.DbmInterceptorFilter.InterceptorType;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

public class DefaultDbmInterceptorChain implements DbmInterceptorChain {
	
	public static final int STATE_INIT = 0;
	public static final int STATE_EXECUTING = 1;
	public static final int STATE_EXECUTED = 2;

	final private Object targetObject;
	final private Method targetMethod;
	final private Object[] targetArgs;

	final private Supplier<Object> actualInvoker;
	
	private LinkedList<DbmInterceptor> interceptors;
	private Iterator<DbmInterceptor> iterator;
	private Object result;
	private int state = STATE_INIT;
	
	final private InterceptorType type;

	public DefaultDbmInterceptorChain(InterceptorType type, Object targetObject, Method targetMethod,
			Object[] targetArgs, Collection<DbmInterceptor> interceptors) {
		super();
		this.type = type;
		this.targetObject = targetObject;
		this.targetMethod = targetMethod;
		this.targetArgs = targetArgs;
		this.interceptors = new LinkedList<DbmInterceptor>(interceptors);
		this.actualInvoker = null;
	}

	public DefaultDbmInterceptorChain(InterceptorType type, Collection<DbmInterceptor> interceptors, Supplier<Object> actualInvoker) {
		super();
		this.type = type;
		this.actualInvoker = actualInvoker;
		this.interceptors = new LinkedList<DbmInterceptor>(interceptors);
		this.targetObject = null;
		this.targetMethod = null;
		this.targetArgs = null;
	}
	
	private void checkState(String msg){
		if(this.state!=STATE_INIT){
			throw new IllegalStateException("illegal state for: "+msg);
		}
	}
	public InterceptorType getType() {
		return type;
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

	public DefaultDbmInterceptorChain addInterceptorToHead(DbmInterceptor...interceptors){
		this.checkState("addInterceptorToHead");
		if(LangUtils.isEmpty(interceptors)){
			return this;
		}
		this.interceptors.addAll(0, Arrays.asList(interceptors));
		return this;
	}
	
	public DefaultDbmInterceptorChain addInterceptorToTail(DbmInterceptor...interceptors){
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
	public DefaultDbmInterceptorChain addInterceptor(DbmInterceptor...interceptors){
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
				result = ReflectUtils.invokeMethod(targetMethod, targetObject, targetArgs);
			}
			state = STATE_EXECUTED;
		}
		return result;
	}

	@Override
	public Object getResult() {
		return result;
	}

}
