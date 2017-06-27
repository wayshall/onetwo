package org.onetwo.boot.core.web;

import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Properties;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.web.mvc.exception.BootWebExceptionResolver;
import org.onetwo.boot.core.web.mvc.interceptor.WebInterceptorAdapter;
import org.onetwo.common.spring.converter.IntStringValueToEnumConverterFactory;
import org.onetwo.common.spring.converter.IntegerToEnumConverterFactory;
import org.onetwo.common.spring.mvc.annotation.BootMvcArgumentResolver;
import org.onetwo.common.spring.mvc.args.ListParameterArgumentResolver;
import org.onetwo.common.spring.mvc.args.WebAttributeArgumentResolver;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/****
 * 有关web mvc的一些扩展配置
 * @author way
 *
 */
public class BootMvcConfigurerAdapter extends WebMvcConfigurerAdapter implements InitializingBean {
	
//	@Autowired
	private BootWebExceptionResolver bootWebExceptionResolver;

	@Autowired
	private BootJFishConfig jfishBootConfig;

	@Autowired
	private List<HandlerInterceptor> interceptorList;

	@Autowired
	private List<HandlerMethodArgumentResolver> argumentResolverList;
	
	@Override
    public void afterPropertiesSet() throws Exception {
//		Assert.notNull(bootWebExceptionResolver);
    }
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new WebAttributeArgumentResolver());
		argumentResolvers.add(new ListParameterArgumentResolver());
		if(this.argumentResolverList!=null){
			argumentResolverList.forEach(arg->{
				if(arg.getClass().getAnnotation(BootMvcArgumentResolver.class)!=null){
					argumentResolvers.add(arg);
				}
			});
		}
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		Properties mediaTypes = jfishBootConfig.getMvc().getMediaTypes();
		if (!CollectionUtils.isEmpty(mediaTypes)) {
			for (Entry<Object, Object> entry : mediaTypes.entrySet()) {
				String extension = ((String)entry.getKey()).toLowerCase(Locale.ENGLISH);
				configurer.mediaType(extension, MediaType.valueOf((String) entry.getValue()));
			}
		}
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		/*Optional.ofNullable(interceptorList).ifPresent(list->{
			list.stream().forEach(inter->registry.addInterceptor(inter));
		});*/
		if(LangUtils.isEmpty(interceptorList)){
			return ;
		}
		for(HandlerInterceptor inter : interceptorList){
			InterceptorRegistration reg = registry.addInterceptor(inter);
			if(inter instanceof WebInterceptorAdapter){
				WebInterceptorAdapter webinter = (WebInterceptorAdapter) inter;
				if(LangUtils.isEmpty(webinter.getPathPatterns())){
					continue;
				}
				reg.addPathPatterns(webinter.getPathPatterns());
			}
		}
//		registry.addInterceptor(new BootFirstInterceptor());
	}

	@Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		if(bootWebExceptionResolver!=null){
			exceptionResolvers.add(bootWebExceptionResolver);
		}
    }

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverterFactory(new IntStringValueToEnumConverterFactory());
		registry.addConverterFactory(new IntegerToEnumConverterFactory());
	}

	/***
	 * WebMvcConfigurationSupport.getMessageConverters
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
	}
	
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters){
		/*AutoWrapJackson2HttpMessageConverter bootJson = new AutoWrapJackson2HttpMessageConverter();
		CUtils.replaceOrAdd(converters, MappingJackson2HttpMessageConverter.class, bootJson);*/
	}

}
