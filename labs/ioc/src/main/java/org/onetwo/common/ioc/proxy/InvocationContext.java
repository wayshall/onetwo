package org.onetwo.common.ioc.proxy;

import java.lang.reflect.Method;
import java.util.Iterator;

public interface InvocationContext {

	public Object[] getArguments();

	public void setArguments(Object[] arguments);

	public Method getMethod();
	
	public Object getTarget();

	public Object proceed();

	public Iterator<BFInterceptor> getIntercptors();

	public void setIntercptors(Iterator<BFInterceptor> intercptors);
	
	public Method getActualMethod();

}