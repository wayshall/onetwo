package org.onetwo.plugins.dq;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.fish.JFishEntityManager;
import org.onetwo.common.fish.spring.JNamedQueryKey;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.ClassUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.ReflectUtils;
import org.slf4j.Logger;

public class DynamicQueryHandler implements InvocationHandler, DynamicQueryProxyFactory {
	
	protected Logger logger = MyLoggerFactory.getLogger(this.getClass());

	private JFishEntityManager em;
	private Object proxyObject;
	private List<Method> excludeMethods = new ArrayList<Method>();
//	private Map<String, Method> methodCache = new HashMap<String, Method>();
	
	public DynamicQueryHandler(JFishEntityManager em, Class<?>... proxiedInterfaces){
//		Class[] proxiedInterfaces = srcObject.getClass().getInterfaces();
//		Assert.notNull(em);
		this.em = em;
		Method[] methods = Object.class.getDeclaredMethods();
		for (int j = 0; j < methods.length; j++) {
			Method method = methods[j];
			excludeMethods.add(method);
		}
		
		this.proxyObject = Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(), proxiedInterfaces, this);
		
	}
	

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if(excludeMethods.contains(method)){
			logger.info("ignore...");
			return ReflectUtils.invokeMethod(method, this, args);
		}

		logger.info("proxy: {}, method: {}", proxy, method);
		String queryName = method.getName();
		Class<?> resultClass = method.getReturnType();
		Class<?> componentClass = ReflectUtils.getGenricType(method.getGenericReturnType(), 0);
		logger.info("resultClass: {}, componentClass:{}", resultClass, componentClass);
		
		if(Page.class.isAssignableFrom(resultClass)){
			if(!Page.class.isInstance(args[0])){
				throw new BaseException("the first arg of method must be a Page object: " + method.toGenericString());
			}
			Page<?> page = (Page<?>)args[0];
			Object[] methodArgs = ArrayUtils.remove(args, 0);
			methodArgs = appendEntityClass(componentClass, methodArgs);
			return em.findPageByQName(queryName, page, methodArgs);
			
		}else if(List.class.isAssignableFrom(resultClass)){
			Object[] params = appendEntityClass(componentClass, args);
			return em.findListByQName(queryName, params);
			
		}else{
			Object[] params = appendEntityClass(componentClass, args);
			return em.findUniqueByQName(queryName, params);
		}
	}

	public Object[] appendEntityClass(Class<?> entityClass, Object[] args){
		if(entityClass==null)
			return args;
		Object[] result = ArrayUtils.addAll(args, new Object[]{JNamedQueryKey.ResultClass, entityClass});
		return result;
	}
	@Override
	public Object getProxyObject(){
		return this.proxyObject;
	}
	

}
