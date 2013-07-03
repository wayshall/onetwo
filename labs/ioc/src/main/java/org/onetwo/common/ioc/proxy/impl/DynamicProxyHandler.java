package org.onetwo.common.ioc.proxy.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.onetwo.common.ioc.proxy.BFInterceptor;
import org.onetwo.common.ioc.proxy.BFProxyHandler;
import org.onetwo.common.ioc.proxy.InvocationContext;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.ReflectUtils;
import org.slf4j.Logger;

public class DynamicProxyHandler implements BFProxyHandler {
	
	protected Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	private Object srcObject;
	private List<BFInterceptor> intercptors;
	private Object proxyObject;
	private List<Method> proxyMethods = new ArrayList<Method>();
//	private Map<String, Method> methodCache = new HashMap<String, Method>();
	
	public DynamicProxyHandler(Object object, Class<?>[] proxiedInterfaces, List<BFInterceptor> intercptors){
		this.srcObject = object;
		this.intercptors = intercptors;
//		Class[] proxiedInterfaces = srcObject.getClass().getInterfaces();

		for (int i = 0; i < proxiedInterfaces.length; i++) {
			Class<?> proxiedInterface = proxiedInterfaces[i];
			Method[] methods = proxiedInterface.getDeclaredMethods();
			for (int j = 0; j < methods.length; j++) {
				Method method = methods[j];
				proxyMethods.add(method);
			}
		}
		
		this.proxyObject = Proxy.newProxyInstance(srcObject.getClass().getClassLoader(), proxiedInterfaces, this);
		
	}
	
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if (other == null) {
			return false;
		}

		DynamicProxyHandler otherProxy = null;
		if (!(other instanceof Proxy)) 
			return false;
		
		otherProxy = (DynamicProxyHandler) Proxy.getInvocationHandler(other);
		
		return getSrcObject().equals(otherProxy.getSrcObject());
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if(this.intercptors==null || !proxyMethods.contains(method)){
			if(ReflectUtils.isEqualsMethod(method)){
				return equals(args[0]);
			}
			return ReflectUtils.invokeMethod(method, srcObject, args);
		}
		Iterator<BFInterceptor> intercptorsIterators = this.intercptors.iterator();
		Method srcMethod = getSrcMethod(method);
		InvocationContext context = new DefaultInvocationContext(srcObject, method, args, srcMethod);
		context.setIntercptors(intercptorsIterators);
		return context.proceed();
	}
	
	/*protected Method getSrcMethodFromCache(Method method){
		Method srcMethod = null;
		srcMethod = methodCache.get(method.toGenericString());
		if(srcMethod!=null)
			return srcMethod;
		
		srcMethod = ReflectUtils.findMethod(srcObject.getClass(), method.getName(), method.getParameterTypes());
		methodCache.put(method.toGenericString(), srcMethod);
		return srcMethod;
	}*/
	
	protected Method getSrcMethod(Method method){
		Method srcMethod = ReflectUtils.findMethod(srcObject.getClass(), method.getName(), method.getParameterTypes());
		return srcMethod;
	}
	

	public Object getSrcObject() {
		return srcObject;
	}
	
	public Object getProxyObject(){
		return this.proxyObject;
	}

}
