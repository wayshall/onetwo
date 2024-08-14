package org.onetwo.cloud.zuul.bugfix;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.CallbackFilter;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.cglib.proxy.NoOp;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.web.ZuulController;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;

public class ZuulHandlerMappingPostProcessor implements BeanPostProcessor {

	private final RouteLocator routeLocator;
    private final ZuulController zuulController;
    private final boolean hasErrorController;

    public ZuulHandlerMappingPostProcessor(RouteLocator routeLocator, ZuulController zuulController, ErrorController errorController) {
      this.routeLocator = routeLocator;
      this.zuulController = zuulController;
      this.hasErrorController = (errorController != null);
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (hasErrorController && (bean instanceof ZuulHandlerMapping)) {
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(ZuulHandlerMapping.class);
			
			enhancer.setCallbackFilter(new CallbackFilter() {
				@Override
				public int accept(Method method) {
					if ("lookupHandler".equals(method.getName())) {
				        return 0;
			        }
			        return 1;
				}
			}); // only for lookupHandler
			
			enhancer.setCallbacks(new Callback[] {new LookupHandlerMethodInterceptor(), NoOp.INSTANCE});
			Constructor<?> ctor = ZuulHandlerMapping.class.getConstructors()[0];
			return enhancer.create(ctor.getParameterTypes(), new Object[] {routeLocator, zuulController});
		}
		return bean;
    }
    
    static public class LookupHandlerMethodInterceptor implements MethodInterceptor {

    	/***
    	 * 拦截ZuulHandlerMapping#lookupHandler方法的调用
    	 * 修复因为zuul依赖的errorController的版本（spring boot 2.4.8）太旧（当前spring boot为2.7.18）导致的java.lang.NoSuchMethodError
    	 */
		@Override
		public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
			// args[0] = urlPath
			if ("/error".equals(args[0])) {
		        return null;
		    }
		    return methodProxy.invokeSuper(target, args);
		}
    	
    }

}
