package org.onetwo.common.ioc.proxy.impl;

import java.lang.reflect.Method;
import java.util.Iterator;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.ioc.proxy.BFInterceptor;
import org.onetwo.common.ioc.proxy.InvocationContext;
import org.onetwo.common.utils.ReflectUtils;

public class DefaultInvocationContext implements InvocationContext {

	private Iterator<BFInterceptor> intercptors;
	private Method method;
	private Object target;
	private Object[] arguments;
	private Method actualMethod;

	

	public DefaultInvocationContext(Object target, Method method, Object[] arguments) {
		this(target, method, arguments, null);
	}

	public DefaultInvocationContext(Object target, Method method, Object[] arguments, Method actualMethod) {
		this.method = method;
		this.target = target;
		this.arguments = arguments;
		this.actualMethod = actualMethod;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	public Method getMethod() {
		return method;
	}

	public Object getTarget() {
		return target;
	}

	public Object proceed() {
		BFInterceptor interceptor = null;
		Object result = null;
		if(this.intercptors!=null && this.intercptors.hasNext()){
			interceptor = this.intercptors.next();
			try {
				result = interceptor.intercept(this);
			} catch (Exception e) {
				throw new ServiceException("invoke interceptor["+interceptor.getClass()+"] error .", e);
			}
		}else{
			result = ReflectUtils.invokeMethod(method, target, arguments);
		}
		return result;
	}

	public Iterator<BFInterceptor> getIntercptors() {
		return intercptors;
	}

	public void setIntercptors(Iterator<BFInterceptor> intercptors) {
		this.intercptors = intercptors;
	}

	public Method getActualMethod() {
		return actualMethod;
	}

}
