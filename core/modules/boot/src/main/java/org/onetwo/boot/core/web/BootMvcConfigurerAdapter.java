package org.onetwo.boot.core.web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.config.BootJFishConfig.CorsConfig;
import org.onetwo.boot.core.config.BootJFishConfig.ResourceHandlerConfig;
import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.web.async.AsyncMvcConfiguration;
import org.onetwo.boot.core.web.async.MvcAsyncProperties;
import org.onetwo.boot.core.web.mvc.exception.BootWebExceptionResolver;
import org.onetwo.boot.core.web.mvc.interceptor.WebInterceptorAdapter;
import org.onetwo.boot.core.web.view.ExtJackson2HttpMessageConverter;
import org.onetwo.boot.utils.BootUtils;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.spring.converter.IntStringValueToEnumConverterFactory;
import org.onetwo.common.spring.converter.IntegerToEnumConverterFactory;
import org.onetwo.common.spring.converter.StringToJackson2ObjectNodeConverterFactory;
import org.onetwo.common.spring.converter.StringToMapConverterFactory;
import org.onetwo.common.spring.mvc.annotation.BootMvcArgumentResolver;
import org.onetwo.common.spring.mvc.args.ListParameterArgumentResolver;
import org.onetwo.common.spring.mvc.args.WebAttributeArgumentResolver;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.dbm.spring.mvc.DbmIntStringValueToEnumConverterFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.accept.PathExtensionContentNegotiationStrategy;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;

/****
 * 有关web mvc的一些扩展配置
 * @author way
 *
 */
public class BootMvcConfigurerAdapter implements WebMvcConfigurer, InitializingBean {
	
//	@Autowired //注释，由spring自动检测添加
	private BootWebExceptionResolver bootWebExceptionResolver;

	@Autowired
	private BootJFishConfig jfishBootConfig;
	@Autowired
	private BootSiteConfig siteConfig;

	@Autowired
	private List<HandlerMethodArgumentResolver> argumentResolverList;
	/***
	 * WebArgumentResolver实际上会通过ServletWebArgumentResolverAdapter适配为HandlerMethodArgumentResolver
	 * 而这个适配实际上调用了两次WebArgumentResolver#resolveArgument，完全没必要，建议不要使用WebArgumentResolver接口
	 */
	@Autowired(required=false)
	private List<WebArgumentResolver> webArgumentResolverList;
	
	@Autowired(required=false)
	private ExtJackson2HttpMessageConverter jackson2HttpMessageConverter;
	
	@Autowired(required=false)
	@Qualifier(AsyncMvcConfiguration.MVC_ASYNC_TASK_BEAN_NAME)
	private AsyncTaskExecutor asyncTaskExecutor;
	@Autowired(required=false)
	private MvcAsyncProperties mvcAsyncProperties;

	@Autowired
	private List<HandlerInterceptor> interceptorList;
//	@Autowired
//	private ApplicationContext applicationContext;
	
	@Override
    public void afterPropertiesSet() throws Exception {
//		Assert.notNull(bootWebExceptionResolver);
    }
	

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		if (!jfishBootConfig.getMvc().isCorsFilter()) {
			addCorsMappings(registry, this.jfishBootConfig.getMvc().getCors());
		}
	}

	static public void addCorsMappings(CorsRegistry registry, List<CorsConfig> corsConfigs) {
		corsConfigs.forEach(corsConfig -> {
			registry.addMapping(corsConfig.getMapping())
					.allowedHeaders(corsConfig.getAllowedHeaders())
					.allowedMethods(corsConfig.getAllowedMethods())
					.allowedOrigins(corsConfig.getAllowedOrigins())
					.allowCredentials(corsConfig.isAllowCredentials())
					.exposedHeaders(corsConfig.getExposedHeaders())
					.maxAge(corsConfig.getMaxAgeInMillis());
		});
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
	public void configureAsyncSupport(AsyncSupportConfigurer configurer){
		if(asyncTaskExecutor!=null){
			configurer.setTaskExecutor(asyncTaskExecutor);
		}
		if(mvcAsyncProperties!=null){
			configurer.setDefaultTimeout(mvcAsyncProperties.getTimeout());
		}
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
		if(this.webArgumentResolverList!=null){
			webArgumentResolverList.forEach(arg->{
				if(arg.getClass().getAnnotation(BootMvcArgumentResolver.class)!=null){
					argumentResolvers.add(new ServletWebArgumentResolverAdapter(arg));
				}
			});
		}
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		Properties mediaTypes = jfishBootConfig.getMvc().getMediaTypes();
		if (!CollectionUtils.isEmpty(mediaTypes)) {
			Map<String, MediaType> addMediaTypes = new HashMap<String, MediaType>();
			for (Entry<Object, Object> entry : mediaTypes.entrySet()) {
				String extension = ((String)entry.getKey()).toLowerCase(Locale.ENGLISH);
				addMediaTypes.put(extension, MediaType.valueOf((String) entry.getValue()));
			}
			configurer.mediaTypes(addMediaTypes);
			configurer.strategies(Arrays.asList(
						new PathExtensionContentNegotiationStrategy(addMediaTypes)
					));
		}
	}
	
	/***
	 * 添加到exceptionResolvers里的HandlerExceptionResolver，会统一由 HandlerExceptionResolverComposite 组合分派
	 * 实际上如果HandlerExceptionResolver本身是spring bean，dispather会自动扫描检测并添加到handlerExceptionResolvers
	 */
	@Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		if(bootWebExceptionResolver!=null){
			exceptionResolvers.add(bootWebExceptionResolver);
		}
    }

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.removeConvertible(String.class, Enum.class);
		if (BootUtils.isEnableDbmPresent()) {
			registry.addConverterFactory(new DbmIntStringValueToEnumConverterFactory());
		} else {
			registry.addConverterFactory(new IntStringValueToEnumConverterFactory());
		}
		registry.addConverterFactory(new IntegerToEnumConverterFactory());
		
		registry.addConverterFactory(new StringToMapConverterFactory());
		registry.addConverterFactory(new StringToJackson2ObjectNodeConverterFactory());
	}

	/***
	 * WebMvcConfigurationSupport.getMessageConverters
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
	}
	
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters){
		if(jackson2HttpMessageConverter!=null){
			CUtils.replaceOrAdd(converters, MappingJackson2HttpMessageConverter.class, jackson2HttpMessageConverter);
		}
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		/*registry.addResourceHandler(UploadViewController.CONTROLLER_PATH+"/**")
				.setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS));*/
		//默认把上传目录映射
		String patterns = siteConfig.getUpload().getAccessPathPatterns();
		if(StringUtils.isNotBlank(patterns)){
			registry.addResourceHandler(patterns)
					.addResourceLocations("file:"+FileUtils.convertDir(siteConfig.getUpload().getFileStorePath()))
					.setCacheControl(CacheControl.maxAge(siteConfig.getUpload().getResourceCacheInDays(), TimeUnit.DAYS));
		}
		List<ResourceHandlerConfig> resourceHandlers = this.jfishBootConfig.getMvc().getResourceHandlers();
		resourceHandlers.forEach(res->{
			registry.addResourceHandler(res.getPathPatterns())
					.addResourceLocations(res.getLocations())
					.setCacheControl(CacheControl.maxAge(res.getCacheInDays(), TimeUnit.DAYS));
		});
	}

}
