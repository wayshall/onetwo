package org.onetwo.boot.core.web.mvc.interceptor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.mvc.HandlerMappingListener;
import org.onetwo.boot.core.web.mvc.annotation.Interceptor;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;

/**
 * @author wayshall
 * <br/>
 */
public class MvcInterceptorManager extends WebInterceptorAdapter implements HandlerMappingListener, ApplicationContextAware, HandlerInterceptor {
	
	private Cache<Method, HandlerMethodInterceptorMeta> interceptorMetaCaces = CacheBuilder.newBuilder().build();
	private ApplicationContext applicationContext;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		boolean executed = executeInterceptors(handler, (hmethod, inter)->{
			inter.preHandle(request, response, hmethod);
		});
		return executed;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		executeInterceptors(handler, (hmethod, inter)->{
			inter.postHandle(request, response, hmethod, modelAndView);
		});
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		executeInterceptors(handler, (hmethod, inter)->{
			inter.afterCompletion(request, response, hmethod, ex);
		});
	}
	
	private boolean executeInterceptors(Object handler, BiConsumer<HandlerMethod, MvcInterceptor> consumer){
		HandlerMethod hmethod = getHandlerMethod(handler);
		if(hmethod==null){
			return false;
		}
		getInterceptorMeta(hmethod.getMethod()).ifPresent(meta->{
			meta.getInterceptors().forEach(inter->consumer.accept(hmethod, inter));
		});
		return true;
	}
	
	private Optional<HandlerMethodInterceptorMeta> getInterceptorMeta(Method method){
		HandlerMethodInterceptorMeta meta = this.interceptorMetaCaces.getIfPresent(method);
		return Optional.ofNullable(meta);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onHandlerMethodsInitialized(Map<RequestMappingInfo, HandlerMethod> handlerMethods) {
		for(HandlerMethod hm : handlerMethods.values()){
			Optional<AnnotationAttributes> attrsOpt = findInterceptorAttrs(hm);
			if(attrsOpt.isPresent()){
				AnnotationAttributes attrs = attrsOpt.get();
				Class<? extends MvcInterceptor>[] interClasses = (Class<? extends MvcInterceptor>[])attrs.get("value");
				List<? extends MvcInterceptor> interceptors = Stream.of(interClasses)
																	.flatMap(cls->SpringUtils.getBeans(applicationContext, cls).stream())
																	.collect(Collectors.toList());
				if(!interceptors.isEmpty()){
					HandlerMethodInterceptorMeta meta = new HandlerMethodInterceptorMeta(hm, interceptors);
					interceptorMetaCaces.put(hm.getMethod(), meta);
				}
			}
		}
	}
	
	private Optional<AnnotationAttributes> findInterceptorAttrs(HandlerMethod hm){
		AnnotationAttributes attrs = AnnotatedElementUtils.getMergedAnnotationAttributes(hm.getMethod(), Interceptor.class);
		if(attrs!=null){
			return Optional.of(attrs);
		}
		Class<?> clazz = hm.getBeanType();
		while(clazz!=Object.class){
			attrs = AnnotatedElementUtils.getMergedAnnotationAttributes(clazz, Interceptor.class);
			if(attrs!=null){
				return Optional.of(attrs);
			}
			clazz = clazz.getSuperclass();
		}
		return Optional.empty();
	}

	@Override
	public int getOrder() {
		return after(FIRST);
	}
	
	static class HandlerMethodInterceptorMeta {
		final private HandlerMethod handlerMethod;
		final private List<? extends MvcInterceptor> interceptors;
		public HandlerMethodInterceptorMeta(HandlerMethod handlerMethod, List<? extends MvcInterceptor> interceptors) {
			super();
			this.handlerMethod = handlerMethod;
			this.interceptors = ImmutableList.copyOf(interceptors);
		}
		public HandlerMethod getHandlerMethod() {
			return handlerMethod;
		}
		public List<? extends MvcInterceptor> getInterceptors() {
			return interceptors;
		}
	}
	

}
