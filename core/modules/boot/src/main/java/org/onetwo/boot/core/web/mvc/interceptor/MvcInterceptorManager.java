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
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
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
	private static final String INTERCEPTORS_KEY = MvcInterceptorManager.class.getName() + ".interceptors";
	
	private Cache<Method, HandlerMethodInterceptorMeta> interceptorMetaCaces = CacheBuilder.newBuilder().build();
	private ApplicationContext applicationContext;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		/*boolean executed = executeInterceptors(handler, (hmethod, inter)->{
			inter.preHandle(request, response, hmethod);
		});*/
		
		HandlerMethod hmethod = getHandlerMethod(handler);
		if(hmethod==null){
			//非HandlerMethod，直接返回true继续mvc的preHandle拦截器，但不执行jfish的preHandle拦截器
			return true;
		}
		Optional<HandlerMethodInterceptorMeta> meta = getInterceptorMeta(hmethod.getMethod());
		if(!meta.isPresent()){
			return true;
		}
		
		List<? extends MvcInterceptor> interceptors = meta.get().getInterceptors();
		request.setAttribute(INTERCEPTORS_KEY, interceptors);
		for(MvcInterceptor inter : interceptors){
			boolean nextPreHandle = inter.preHandle(request, response, hmethod);
			if(!nextPreHandle){
				return nextPreHandle;
			}
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		executeInterceptors(request, handler, (hmethod, inter)->{
			inter.postHandle(request, response, hmethod, modelAndView);
		});
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		executeInterceptors(request, handler, (hmethod, inter)->{
			inter.afterCompletion(request, response, hmethod, ex);
		});
	}
	
	@SuppressWarnings("unchecked")
	private void executeInterceptors(HttpServletRequest request, Object handler, BiConsumer<HandlerMethod, MvcInterceptor> consumer){
		/*HandlerMethod hmethod = getHandlerMethod(handler);
		if(hmethod==null){
			return ;
		}
		getInterceptorMeta(hmethod.getMethod()).ifPresent(meta->{
			meta.getInterceptors().forEach(inter->consumer.accept(hmethod, inter));
		});*/
		
		List<? extends MvcInterceptor> interceptors = (List<? extends MvcInterceptor>)request.getAttribute(INTERCEPTORS_KEY);
		if(interceptors==null){
			return ;
		}
		HandlerMethod hmethod = getHandlerMethod(handler);
		interceptors.forEach(inter->consumer.accept(hmethod, inter));
		return ;
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
																	.flatMap(cls->{
																		List<? extends MvcInterceptor> inters = SpringUtils.getBeans(applicationContext, cls);
																		if(LangUtils.isEmpty(inters)){
																			throw new BaseException("MvcInterceptor not found for : " + cls);
																		}
																		return inters.stream();
																	})
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
		if(attrs==null){
			attrs = AnnotatedElementUtils.getMergedAnnotationAttributes(hm.getBeanType(), Interceptor.class);
		}
		return Optional.ofNullable(attrs);
	}
	
	private Optional<AnnotationAttributes> findInterceptorAttrs2(HandlerMethod hm){
		AnnotationAttributes attrs = AnnotatedElementUtils.getMergedAnnotationAttributes(hm.getMethod(), Interceptor.class);
		if(attrs==null){
			attrs = AnnotatedElementUtils.getMergedAnnotationAttributes(hm.getBeanType(), Interceptor.class);
		}
		return Optional.ofNullable(attrs);
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
