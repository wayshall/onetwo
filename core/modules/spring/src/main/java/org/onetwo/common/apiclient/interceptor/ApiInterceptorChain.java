package org.onetwo.common.apiclient.interceptor;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.onetwo.common.utils.LangUtils;

/**
 * @author weishao zeng
 * <br/>
 */
public class ApiInterceptorChain {
	
	public static interface ActionInvoker {
		Object invoke() throws Throwable;
	}

	final private Object requestContext;
	final private ActionInvoker actualInvoker;
	private Collection<ApiInterceptor> interceptors;
	private Iterator<ApiInterceptor> iterator;
	private Object result;
//	private Throwable throwable;
	

	public ApiInterceptorChain(Collection<ApiInterceptor> interceptors, Object context, ActionInvoker actualInvoker) {
		super();
		if (LangUtils.isNotEmpty(interceptors)) {
//			AnnotationAwareOrderComparator.sort(interceptors);
			this.interceptors = new LinkedList<>(interceptors);
		} else {
			this.interceptors = Collections.emptyList();
		}
		this.actualInvoker = actualInvoker;
		this.requestContext = context;
	}

	/****
	 * 依次调用拦截器，最后调用实际请求actualInvoker.invoke()；
	 * 返回结果后，再逆序进入拦截器
	 * @author weishao zeng
	 * @return
	 * @throws Throwable
	 */
	public Object invoke() throws Throwable {
		if(iterator==null){
			this.iterator = this.interceptors.iterator();
		}
		if(iterator.hasNext()){
			ApiInterceptor interceptor = iterator.next();
			result = interceptor.intercept(this);
		}else{
//			result = actualInvoker.get();
			result = actualInvoker.invoke();
		}
		return result;
	}
	
	/***
	 * 重置拦截器调用栈状态，重置后，当调用 {@link ApiInterceptorChain#invoke()} 的时候会重新调用一遍拦截器
	 * @author weishao zeng
	 */
	void restInvokeState() {
		this.iterator = null;
	}
	
	Object getResult() {
		return result;
	}
	
//	Throwable getThrowable() {
//		return throwable;
//	}

	public Object getRequestContext() {
		return requestContext;
	}
}

