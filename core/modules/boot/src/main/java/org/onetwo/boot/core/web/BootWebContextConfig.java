package org.onetwo.boot.core.web;

import java.util.List;

import org.onetwo.boot.core.BootContextConfig;
import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.config.JFishBootConfig;
import org.onetwo.boot.core.init.BootServletContextInitializer;
import org.onetwo.boot.core.web.ftl.FreemarkerViewContextConfig;
import org.onetwo.boot.core.web.mvc.BootWebExceptionResolver;
import org.onetwo.boot.core.web.mvc.RequestMappingHandlerMappingListenable;
import org.onetwo.boot.core.web.view.BootJsonView;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

@Configuration
//@EnableConfigurationProperties({JFishBootConfig.class, SpringBootConfig.class})
@Import({BootContextConfig.class, FreemarkerViewContextConfig.class})
public class BootWebContextConfig {
	

	@Autowired
	private JFishBootConfig jfishBootConfig;
	
	@Bean
	public BootSiteConfig bootSiteConfig(){
		return BootSiteConfig.getInstance();
	}
	
	@Bean
	public BootServletContextInitializer bootServletContextInitializer(){
		return new BootServletContextInitializer();
	}
	
	@Bean
	RequestMappingHandlerMappingListenable requestMappingHandlerMappingListenable(){
		RequestMappingHandlerMappingListenable req = new RequestMappingHandlerMappingListenable();
		return req;	
	}
	
	@Bean
	public BootMvcConfigurerAdapter bootMvcConfigurerAdapter(){
		return new BootMvcConfigurerAdapter();
	}
	
	@Bean
	public BootWebExceptionResolver bootWebExceptionResolver(){
		return new BootWebExceptionResolver();
	}
	
	@Bean
	public BootJsonView bootJsonView(){
		return new BootJsonView();
	}
	


	@Bean
	@Autowired
	public ViewResolver viewResolver(ApplicationContext applicationContext, ContentNegotiationManager contentNegotiationManager) {
		List<View> views = SpringUtils.getBeans(applicationContext, View.class);
		ContentNegotiatingViewResolver viewResolver = new ContentNegotiatingViewResolver();
		viewResolver.setUseNotAcceptableStatusCode(true);
		viewResolver.setOrder(0);
		viewResolver.setDefaultViews(views);
//		List<View> views = LangUtils.asListWithType(View.class, xmlView(), jsonView());
//		viewResolver.setMediaTypes(mediaType());
//		viewResolver.setDefaultContentType(MediaType.TEXT_HTML);
//		viewResolver.setIgnoreAcceptHeader(true);
		viewResolver.setContentNegotiationManager(contentNegotiationManager);
		return viewResolver;
	}
	
//	@Bean
	/*private ContentNegotiationManagerFactoryBean contentNegotiationManagerFactoryBean(){
		ContentNegotiationManagerFactoryBean bean = new ContentNegotiationManagerFactoryBean();
		bean.setMediaTypes(jfishBootConfig.getMediaType());
		bean.setDefaultContentType(MediaType.TEXT_HTML);
		bean.setIgnoreAcceptHeader(true);
		bean.setFavorParameter(true);
		return bean;
	}*/
	
	/*@Bean
	public BootFirstInterceptor bootFirstInterceptor(){
		return new BootFirstInterceptor();
	}*/

}
