package org.onetwo.plugins.dq;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.fish.JFishEntityManager;
import org.onetwo.common.fish.JFishQuery;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.profiling.TimeCounter;
import org.onetwo.common.utils.ClassUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.ReflectUtils;
import org.slf4j.Logger;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;

public class DynamicQueryHandler implements InvocationHandler {
	
	protected Logger logger = MyLoggerFactory.getLogger(this.getClass());

	private Cache methodCache;
	private JFishEntityManager em;
	private Object proxyObject;
	private List<Method> excludeMethods = new ArrayList<Method>();
//	private ParameterNameDiscoverer pnd = new LocalVariableTableParameterNameDiscoverer();
//	private Map<String, Method> methodCache = new HashMap<String, Method>();
	
	public DynamicQueryHandler(JFishEntityManager em, Cache methodCache, Class<?>... proxiedInterfaces){
//		Class[] proxiedInterfaces = srcObject.getClass().getInterfaces();
//		Assert.notNull(em);
		this.em = em;
		this.methodCache = methodCache;
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
			logger.info("ignore method {} ...", method.toString());
			return ReflectUtils.invokeMethod(method, this, args);
		}

		try {
			return this.doInvoke(proxy, method, args);
		} catch (Throwable e) {
			throw new BaseException("invoke proxy error : " + e.getMessage(), e);
		}
		
	}
	
	protected DynamicMethod getDynamicMethod(Method method){
		if(methodCache!=null){
			ValueWrapper value = methodCache.get(method);
			if(value!=null)
				return (DynamicMethod) value.get();
			DynamicMethod dm = new DynamicMethod(method);
			methodCache.put(method, dm);
			return dm;
		}else{
			return new DynamicMethod(method);
		}
	}
	
	public Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable {
//		LangUtils.println("proxy: "+proxy+", method: ${0}", method);
//		TimeCounter t = new TimeCounter("doproxy", true);
//		t.start();
		DynamicMethod dmethod = getDynamicMethod(method);
		Class<?> resultClass = dmethod.getResultClass();
		Class<?> componentClass = dmethod.getComponentClass();
		String queryName = dmethod.getQueryName();
		
		Object result = null;
		if(Page.class.isAssignableFrom(resultClass)){
			Page<?> page = (Page<?>)args[0];
			
			Object[] methodArgs = dmethod.toArrayByArgs(args, componentClass);
//			methodArgs = appendEntityClass(componentClass, methodArgs);
//			t.stop();
			result = em.findPageByQName(queryName, page, methodArgs);
			
		}else if(List.class.isAssignableFrom(resultClass)){
			Object[] methodArgs = dmethod.toArrayByArgs(args, componentClass);
//			t.stop();
			result = em.findListByQName(queryName, methodArgs);
			
		}else if(JFishQuery.class.isAssignableFrom(resultClass)){
			Object[] methodArgs = dmethod.toArrayByArgs(args, null);
//			t.stop();
			JFishQuery dq = em.createJFishQueryByQName(queryName, methodArgs);
			return dq;
			
		}else{
			Object[] methodArgs = dmethod.toArrayByArgs(args, componentClass);
			if(dmethod.isExecuteUpdate()){
				JFishQuery dq = em.createJFishQueryByQName(queryName, methodArgs);
				result = dq.executeUpdate();
			}else{
				result = em.findUniqueByQName(queryName, methodArgs);
			}
		}
		return result;
	}

//	public Object[] appendEntityClass(Class<?> entityClass, Object[] args){
//		if(entityClass==null || entityClass==Object.class)
//			return args;
//		Object[] result = ArrayUtils.addAll(args, new Object[]{JNamedQueryKey.ResultClass, entityClass});
//		return result;
//	}
	
	public Object getQueryObject(){
		return this.proxyObject;
	}
	
}
