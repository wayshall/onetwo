package org.onetwo.boot.core.web.mvc.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.onetwo.boot.core.web.mvc.HandlerMappingListener;
import org.onetwo.boot.core.web.mvc.annotation.Interceptor;
import org.onetwo.common.annotation.AnnotationUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.utils.PropertyAnnotationReader;
import org.onetwo.common.spring.utils.PropertyAnnotationReader.PropertyAnnoMeta;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.BeanWrapper;
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
@Slf4j
public class MvcInterceptorManager extends WebInterceptorAdapter implements HandlerMappingListener, ApplicationContextAware, HandlerInterceptor {
	private static final String INTERCEPTORS_KEY = MvcInterceptorManager.class.getName() + ".interceptors";
	
	private Cache<Method, HandlerMethodInterceptorMeta> interceptorMetaCaces = CacheBuilder.newBuilder().build();
	private ApplicationContext applicationContext;
	private PropertyAnnotationReader propertyAnnotationReader = PropertyAnnotationReader.INSTANCE;

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
			Collection<AnnotationAttributes> attrsList = findInterceptorAttrsList(hm);
			/*if(attrsList.isEmpty()){
				attrsList = findImplicitInterceptorAttrsList(hm);
			}*/
			if(attrsList.isEmpty()){
				continue ;
			}
//			AnnotationAttributes attrs = attrsOpt.get();
//			Class<? extends MvcInterceptor>[] interClasses = (Class<? extends MvcInterceptor>[])attrs.get("value");
			List<? extends MvcInterceptor> interceptors = attrsList.stream()
																.map(attr->{
																	Class<? extends MvcInterceptor> cls = (Class<? extends MvcInterceptor>)attr.getClass("value");
																	List<? extends MvcInterceptor> inters = SpringUtils.getBeans(applicationContext, cls);
																	if(LangUtils.isEmpty(inters)){
//																		throw new BaseException("MvcInterceptor not found for : " + cls);
																		MvcInterceptor interInst = ReflectUtils.newInstance(cls);
																		SpringUtils.injectAndInitialize(applicationContext, interInst);
																		log.info("create and init MvcInterceptor: {}", cls);
																		
																		List<PropertyAnnoMeta> properties = propertyAnnotationReader.readProperties(attr);
																		BeanWrapper bw = SpringUtils.newBeanWrapper(interInst);
																		for(PropertyAnnoMeta prop : properties){
																			bw.setPropertyValue(prop.getName(), prop.getValue());
																		}
																		
																		return interInst;
																	}else if(inters.size()>1){
																		throw new BaseException("multip MvcInterceptor found for : " + cls);
																	}else{
																		log.info("found MvcInterceptor from applicationContext: {}", cls);
																	}
																	return inters.get(0);
																})
																.collect(Collectors.toList());
			if(!interceptors.isEmpty()){
				HandlerMethodInterceptorMeta meta = new HandlerMethodInterceptorMeta(hm, interceptors);
				interceptorMetaCaces.put(hm.getMethod(), meta);
			}
			if(log.isInfoEnabled()){
				log.info("MvcInterceptor: {} -> {}", hm.getMethod(), interceptors);
			}
		}
	}
	public void onHandlerMethodsInitialized1(Map<RequestMappingInfo, HandlerMethod> handlerMethods) {
		for(HandlerMethod hm : handlerMethods.values()){
			Optional<AnnotationAttributes> attrsOpt = findExplicitInterceptorAttrs(hm);
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
	
	/***
	 * 查找隐式Interceptor
	 * @author wayshall
	 * @param hm
	 * @return
	 */
	/*private Collection<AnnotationAttributes> findImplicitInterceptorAttrsList(HandlerMethod hm){
		List<Annotation> combines = AnnotationUtils.findCombineAnnotations(hm.getMethod(), Interceptor.class);
		//find on class
		if(LangUtils.isEmpty(combines)){
			combines = AnnotationUtils.findCombineAnnotations(hm.getBeanType(), Interceptor.class);
		}
		
		if(LangUtils.isEmpty(combines)){
			return Collections.emptyList();
		}
		Set<Interceptor> inters = combines.stream()
											.flatMap(combine->AnnotatedElementUtils.getMergedRepeatableAnnotations(combine.annotationType(), Interceptor.class).stream())
											.collect(Collectors.toSet());
//		Set<Interceptor> inters = AnnotatedElementUtils.getMergedRepeatableAnnotations(combines.get(0).annotationType(), Interceptor.class);
		return inters.stream()
					.map(inter->org.springframework.core.annotation.AnnotationUtils.getAnnotationAttributes(null, inter))
					.collect(Collectors.toSet());
	}*/
	
	/***
	 * 直接查找Interceptor
	 * @author wayshall
	 * @param hm
	 * @return
	 */
	private Collection<AnnotationAttributes> findInterceptorAttrsList(HandlerMethod hm){
		//间接包含了@Interceptor的，也能找到……
		Set<Interceptor> inters = AnnotatedElementUtils.getMergedRepeatableAnnotations(hm.getMethod(), Interceptor.class);
		if(LangUtils.isEmpty(inters)){
			inters = AnnotatedElementUtils.getMergedRepeatableAnnotations(hm.getBeanType(), Interceptor.class);
		}
		if(LangUtils.isEmpty(inters)){
			return Collections.emptyList();
		}
		return inters.stream()
					.map(inter->org.springframework.core.annotation.AnnotationUtils.getAnnotationAttributes(null, inter))
					.collect(Collectors.toSet());
	}

	@Deprecated
	private Optional<AnnotationAttributes> findImplicitInterceptorAttrs(HandlerMethod hm){
		List<Annotation> combines = AnnotationUtils.findCombineAnnotations(hm.getMethod(), Interceptor.class);
		//find on class
		if(LangUtils.isEmpty(combines)){
			combines = AnnotationUtils.findCombineAnnotations(hm.getBeanType(), Interceptor.class);
		}
		
		AnnotationAttributes attrs = AnnotatedElementUtils.getMergedAnnotationAttributes(combines.get(0).annotationType(), Interceptor.class);
		return Optional.ofNullable(attrs);
	}
	@Deprecated
	private Optional<AnnotationAttributes> findExplicitInterceptorAttrs(HandlerMethod hm){
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
