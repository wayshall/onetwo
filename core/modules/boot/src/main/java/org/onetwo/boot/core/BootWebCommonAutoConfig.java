package org.onetwo.boot.core;

import javax.annotation.PostConstruct;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.init.BootServletContextInitializer;
import org.onetwo.boot.core.init.ConfigServletContextInitializer;
import org.onetwo.boot.core.web.BootMvcConfigurerAdapter;
import org.onetwo.boot.core.web.filter.BootRequestContextFilter;
import org.onetwo.boot.core.web.mvc.BootStandardServletMultipartResolver;
import org.onetwo.boot.core.web.mvc.RequestMappingHandlerMappingListenable;
import org.onetwo.boot.core.web.mvc.exception.BootWebExceptionHandler;
import org.onetwo.boot.core.web.mvc.exception.BootWebExceptionResolver;
import org.onetwo.boot.core.web.mvc.interceptor.BootFirstInterceptor;
import org.onetwo.boot.core.web.mvc.interceptor.UploadValidateInterceptor;
import org.onetwo.boot.core.web.userdetails.BootSessionUserManager;
import org.onetwo.boot.core.web.view.BootJsonView;
import org.onetwo.boot.json.BootJackson2ObjectMapperBuilder;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.web.userdetails.SessionUserManager;
import org.onetwo.common.web.userdetails.UserDetail;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;

public class BootWebCommonAutoConfig {
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	@Autowired
	protected ApplicationContext applicationContext;

	@Autowired
	protected BootJFishConfig bootJfishConfig;
	
	@Autowired
	protected BootSiteConfig bootSiteConfig;
	
	@PostConstruct
	public void init(){
		SpringApplication.initApplicationIfNotInitialized(applicationContext);
	}
	
	
	@Bean
	public FilterRegistrationBean requestContextFilter(){
		FilterRegistrationBean registration = new FilterRegistrationBean(new BootRequestContextFilter());
		registration.setOrder(Ordered.HIGHEST_PRECEDENCE+100);
		registration.setName("requestContextFilter");
		return registration;
	}
	
	@Bean
	@ConditionalOnMissingBean(BootServletContextInitializer.class)
	public BootServletContextInitializer bootServletContextInitializer(){
		return new BootServletContextInitializer();
	}
	
	@Bean
	public ConfigServletContextInitializer configServletContextInitializer(){
		return new ConfigServletContextInitializer();
	}
	
	@Bean
	public RequestMappingHandlerMappingListenable requestMappingHandlerMappingListenable(){
		RequestMappingHandlerMappingListenable req = new RequestMappingHandlerMappingListenable();
		return req;	
	}
	
	@Bean
	public BootMvcConfigurerAdapter bootMvcConfigurerAdapter(){
		return new BootMvcConfigurerAdapter();
	}
	
	/***
	 * 异常解释
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(BootWebExceptionResolver.class)
//	@Autowired
	public BootWebExceptionResolver bootWebExceptionResolver(){
		BootWebExceptionResolver resolver = new BootWebExceptionResolver();
//		resolver.setExceptionMessage(exceptionMessage);
		return resolver;
	}
	
	@Bean
	public BootWebExceptionHandler bootWebExceptionHandler(){
		return new BootWebExceptionHandler();
	}
	
	/***
	 * 拦截器
	 * @return
	 */
	@Bean
	public BootFirstInterceptor bootFirstInterceptor(){
		return new BootFirstInterceptor();
	}

	@Bean
	public UploadValidateInterceptor uploadValidateInterceptor(){
		return new UploadValidateInterceptor();
	}
	
	@Bean
	public BootJsonView bootJsonView(){
		BootJsonView jv = new BootJsonView();
		jv.setPrettyPrint(bootJfishConfig.getMvc().getJson().isPrettyPrint());
		return jv;
	}
	
	@Bean(name=MultipartFilter.DEFAULT_MULTIPART_RESOLVER_BEAN_NAME)
//	@ConditionalOnMissingBean(MultipartResolver.class)
	public MultipartResolver filterMultipartResolver(){
		BootStandardServletMultipartResolver resolver = new BootStandardServletMultipartResolver();
		resolver.setMaxUploadSize(bootSiteConfig.getUpload().getMaxUploadSize());
		return resolver;
	}

	@Bean
	@ConditionalOnMissingBean(SessionUserManager.class)
	public SessionUserManager<UserDetail> sessionUserManager(){
		return new BootSessionUserManager();
	}

	@Bean
	public BootJackson2ObjectMapperBuilder bootJackson2ObjectMapperBuilder(){
		return new BootJackson2ObjectMapperBuilder();
	}
	

}
