package org.onetwo.plugins.dq;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.db.CreateQueryable;
import org.onetwo.common.db.DataQuery;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.ClassUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.ReflectUtils;
import org.slf4j.Logger;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;

public class DynamicQueryHandler implements InvocationHandler {
	
	protected Logger logger = MyLoggerFactory.getLogger(this.getClass());

	private Cache methodCache;
	private CreateQueryable em;
	private Object proxyObject;
	private List<Method> excludeMethods = new ArrayList<Method>();
	private boolean debug = true;
//	private ParameterNameDiscoverer pnd = new LocalVariableTableParameterNameDiscoverer();
//	private Map<String, Method> methodCache = new HashMap<String, Method>();
	
	public DynamicQueryHandler(CreateQueryable em, Cache methodCache, Class<?>... proxiedInterfaces){
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
			throw new BaseException("invoke query["+method.getDeclaringClass().getSimpleName()+"."+method.getName()+"] error : " + e.getMessage(), e);
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

		if(debug)
			logger.info("{}: {}", method.getName(), LangUtils.toString(args));
		
		Object result = null;
		Object[] methodArgs = null;
		if(Page.class.isAssignableFrom(resultClass)){
			Page<?> page = (Page<?>)args[0];
			
//			Object[] trimPageArgs = ArrayUtils.remove(args, 0);
			methodArgs = dmethod.toArrayByArgs(args, componentClass);
			result = em.getFileNamedQueryFactory().findPage(queryName, page, methodArgs);
			
		}else if(List.class.isAssignableFrom(resultClass)){
			methodArgs = dmethod.toArrayByArgs(args, componentClass);
//			logger.info("dq args: {}", LangUtils.toString(methodArgs));
			result = em.getFileNamedQueryFactory().findList(queryName, methodArgs);
			
		}else if(DataQuery.class.isAssignableFrom(resultClass)){
			methodArgs = dmethod.toArrayByArgs(args, null);
//			logger.info("dq args: {}", LangUtils.toString(methodArgs));
			DataQuery dq = em.getFileNamedQueryFactory().createQuery(queryName, methodArgs);
			return dq;
			
		}else{
			methodArgs = dmethod.toArrayByArgs(args, componentClass);
//			logger.info("dq args: {}", LangUtils.toString(methodArgs));
			if(dmethod.isExecuteUpdate()){
				DataQuery dq = em.getFileNamedQueryFactory().createQuery(queryName, methodArgs);
				result = dq.executeUpdate();
			}else{
				result = em.getFileNamedQueryFactory().findUnique(queryName, methodArgs);
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
