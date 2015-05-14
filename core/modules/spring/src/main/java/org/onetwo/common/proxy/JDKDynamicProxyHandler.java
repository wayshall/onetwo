package org.onetwo.common.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.ClassUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.slf4j.Logger;

abstract public class JDKDynamicProxyHandler implements InvocationHandler {

	final protected Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	final private Object proxyObject;
	private List<Method> excludeMethods = new ArrayList<Method>();
	
	public JDKDynamicProxyHandler(Class<?> excludeTargetClass, Class<?>... proxiedInterfaces){
		if(excludeTargetClass!=null){
			excludeDeclaredMethod(excludeTargetClass);
		}
		this.proxyObject = Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(), proxiedInterfaces, this);
	}
	
	final protected void excludeDeclaredMethod(Class<?> targetClass){
		Method[] methods = targetClass.getDeclaredMethods();
		for (int j = 0; j < methods.length; j++) {
			Method method = methods[j];
			excludeMethods.add(method);
		}
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if(excludeMethods.contains(method)){
			if(logger.isInfoEnabled()){
				logger.info("ignore method | {} ...", method.toString());
			}
			return ReflectUtils.invokeMethod(method, proxy, args);
		}

		try {
			return this.doInvoke(proxy, method, args);
		}catch (Throwable e) {
			throw new BaseException("invoke proxy method error : " + e.getMessage(), e);
		}
		
	}

	public Object getProxyObject() {
		return proxyObject;
	}

	abstract protected Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable;
}
