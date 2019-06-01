package org.onetwo.common.apiclient.interceptor;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Supplier;

import org.onetwo.common.apiclient.RequestContextData;
import org.onetwo.common.utils.LangUtils;

/**
 * @author weishao zeng
 * <br/>
 */
public class ApiInterceptorChain {

	final private RequestContextData requestContext;
	final private Supplier<Object> actualInvoker;
	private Collection<ApiInterceptor> interceptors;
	private Iterator<ApiInterceptor> iterator;
	private Object result;
	private Throwable throwable;
	

	public ApiInterceptorChain(Collection<ApiInterceptor> interceptors, RequestContextData context, Supplier<Object> actualInvoker) {
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

	public Object invoke(){
		if(iterator==null){
			this.iterator = this.interceptors.iterator();
		}
		if(iterator.hasNext()){
			ApiInterceptor interceptor = iterator.next();
			result = interceptor.intercept(this);
		}else{
			result = actualInvoker.get();
		}
		return result;
	}
	
	Object getResult() {
		return result;
	}
	
	Throwable getThrowable() {
		return throwable;
	}

	public RequestContextData getRequestContext() {
		return requestContext;
	}
}

