package org.onetwo.common.apiclient.impl;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.onetwo.common.apiclient.interceptor.ApiInterceptorChain;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.proxy.AbstractMethodInterceptor;
import org.onetwo.common.proxy.BaseMethodParameter;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.spring.aop.MixinableInterfaceCreator;
import org.onetwo.common.spring.aop.SpringMixinableInterfaceCreator;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.cache.Cache;

/**
 * @author wayshall
 * <br/>
 */
abstract public class BaseApiClientFactoryBean<P extends BaseMethodParameter, M extends BaseApiClientMethod<P>> implements FactoryBean<Object>, InitializingBean, ApplicationContextAware {

//	final protected LoadingCache<Method, M> apiMethodCaches = CacheBuilder.newBuilder()
//																.build(new CacheLoader<Method, M>() {
//																	@Override
//																	public M load(Method method) throws Exception {
//																		M apiMethod = createApiMethod(method);
//																		return apiMethod;
//																	}
//																});
//	
	final protected Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	protected Class<?> interfaceClass;
	protected Object apiObject;

//	protected ApiErrorHandler apiErrorHandler;
	protected ApplicationContext applicationContext;
	

	/*public void setApiErrorHandler(ApiErrorHandler apiErrorHandler) {
		this.apiErrorHandler = apiErrorHandler;
	}*/

	/*public void setMaxRetryCount(int maxRetryCount) {
		this.maxRetryCount = maxRetryCount;
	}*/

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		/*if (apiErrorHandler == null) {
			apiErrorHandler = ApiErrorHandler.DEFAULT;
		}*/
	}
	
	abstract protected M createApiMethod(Method method);
	
	abstract protected MethodInterceptor createApiMethodInterceptor();
	
	abstract protected Object interceptMethod(MethodInvocation invocation, M invokeMethod) throws Throwable;

	@Override
	public Object getObject() throws Exception {
		Object apiObject = this.apiObject;
		if(apiObject==null){
			Springs.initApplicationIfNotInitialized(applicationContext);
			MethodInterceptor apiClient = createApiMethodInterceptor();
			MixinableInterfaceCreator mixinableCreator = SpringMixinableInterfaceCreator.classNamePostfixMixin(interfaceClass);
			apiObject = mixinableCreator.createMixinObject(apiClient);
			this.apiObject = apiObject;
		}
		return apiObject;
	}

	@Override
	public Class<?> getObjectType() {
		return interfaceClass;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setInterfaceClass(Class<?> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}
	
	public class SimpleApiMethodInterceptor extends AbstractMethodInterceptor<M> {

		public SimpleApiMethodInterceptor(Cache<Method, M> methodCache) {
			super(methodCache);
		}

		@Override
		protected Object doInvoke(MethodInvocation invocation, M invokeMethod) throws Throwable {
			ApiInterceptorChain chain = new ApiInterceptorChain(invokeMethod.getInterceptors(), null, () -> {
				return interceptMethod(invocation, invokeMethod);
			});
			return chain.invoke();
			
		}
		

		protected M createMethod(Method method) {
			return createApiMethod(method);
		}
	}

}
