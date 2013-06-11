package org.onetwo.plugins.dq;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.fish.JFishEntityManager;
import org.onetwo.common.fish.JFishQuery;
import org.onetwo.common.fish.spring.JNamedQueryKey;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.ClassUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.plugins.dq.annotations.Name;
import org.slf4j.Logger;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;

public class DynamicQueryHandler implements InvocationHandler, DynamicQueryProxyFactory {
	
	protected Logger logger = MyLoggerFactory.getLogger(this.getClass());

	private JFishEntityManager em;
	private Object proxyObject;
	private List<Method> excludeMethods = new ArrayList<Method>();
	private ParameterNameDiscoverer pnd = new LocalVariableTableParameterNameDiscoverer();
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

		try {
			return this.doInvoke(proxy, method, args);
		} catch (Throwable e) {
			throw new BaseException("invoke proxy error : " + e.getMessage(), e);
		}
		
	}
	
	public List<MethodParameterValue> asMethodParameters(Method method, Object[] args){
		List<MethodParameterValue> methodList = LangUtils.newArrayList(args.length);
		MethodParameter mp = null;
		int index = 0;
		for(Object arg : args){
			mp = new MethodParameter(method, index++);
			mp.initParameterNameDiscovery(pnd);
			System.out.println(method.toGenericString()+", name: " + mp.getParameterName());
			methodList.add(new MethodParameterValue(mp, arg));
		}
		return methodList;
	}
	
	public Object[] methodListAsArray(List<MethodParameterValue> methodParameterList, Class<?> componentClass){
		List<Object> values = LangUtils.newArrayList(methodParameterList.size()*2);
		for(MethodParameterValue mpv : methodParameterList){
			if(!LangUtils.isSimpleTypeObject(mpv.value)){
				Map<?, ?> map = ReflectUtils.toMap(mpv.value);
				for(Entry<?, ?> entry : map.entrySet()){
					values.add(entry.getKey());
					values.add(entry.getValue());
				}
			}else{
				Name name = mpv.methodParameter.getParameterAnnotation(Name.class);
//				Name name = AnnotationUtils.findAnnotation(mpv.methodParameter.getMethod(), Name.class);
				if(name!=null){
					values.add(name.value());
				}else{
					values.add(String.valueOf(mpv.methodParameter.getParameterIndex()));
				}
				values.add(mpv.value);
			}
		}
		if(componentClass!=null){
			values.add(JNamedQueryKey.ResultClass);
			values.add(componentClass);
		}
		return values.toArray();
	}
	
	public Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable {
		LangUtils.println("proxy: "+proxy+", method: ${0}", method);
		String queryName = method.getDeclaringClass().getName()+"."+method.getName();
		Class<?> resultClass = method.getReturnType();
		Class<?> componentClass = ReflectUtils.getGenricType(method.getGenericReturnType(), 0);
		if(componentClass==Object.class)
			componentClass = resultClass;
		LangUtils.println("resultClass: ${0}, componentClass:${1}", resultClass, componentClass);
		
		List<MethodParameterValue> mplist = asMethodParameters(method, args);
		
		Object result = null;
		//for test
//		if(em==null)
//			return LangUtils.isBaseType(resultClass)?Types.convertValue(result, resultClass):null;
		
		if(Page.class.isAssignableFrom(resultClass)){
			if(!Page.class.isInstance(mplist.get(0).value)){
				throw new BaseException("the first arg of method must be a Page object: " + method.toGenericString());
			}
			Page<?> page = (Page<?>)mplist.remove(0).value;
			Object[] methodArgs = methodListAsArray(mplist, componentClass);
//			methodArgs = appendEntityClass(componentClass, methodArgs);
			result = em.findPageByQName(queryName, page, methodArgs);
			
		}else if(List.class.isAssignableFrom(resultClass)){
			Object[] methodArgs = methodListAsArray(mplist, componentClass);
			result = em.findListByQName(queryName, methodArgs);
			
		}else if(JFishQuery.class.isAssignableFrom(resultClass)){
			Object[] methodArgs = methodListAsArray(mplist, null);
			JFishQuery dq = em.createJFishQueryByQName(queryName, methodArgs);
			return dq;
			
		}else{
			Object[] methodArgs = methodListAsArray(mplist, componentClass);
			result = em.findUniqueByQName(queryName, methodArgs);
		}
		return result;
	}

//	public Object[] appendEntityClass(Class<?> entityClass, Object[] args){
//		if(entityClass==null || entityClass==Object.class)
//			return args;
//		Object[] result = ArrayUtils.addAll(args, new Object[]{JNamedQueryKey.ResultClass, entityClass});
//		return result;
//	}
	
	@Override
	public Object getProxyObject(){
		return this.proxyObject;
	}
	
	private static class MethodParameterValue {
		private MethodParameter methodParameter;
		private Object value;
		public MethodParameterValue(MethodParameter methodParameter,
				Object value) {
			super();
			this.methodParameter = methodParameter;
			this.value = value;
		}
		
	}

}
