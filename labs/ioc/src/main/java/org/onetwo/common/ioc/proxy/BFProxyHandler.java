package org.onetwo.common.ioc.proxy;

import java.lang.reflect.InvocationHandler;


public interface BFProxyHandler extends InvocationHandler {
	
	public Object getSrcObject();
	public Object getProxyObject();
	
}
