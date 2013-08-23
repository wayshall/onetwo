package org.onetwo.common.spring.web.mvc.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.onetwo.common.fish.plugin.JFishPluginManagerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.converter.JFishStringToEnumConverterFactory;
import org.onetwo.common.spring.web.mvc.HandlerMappingListener;
import org.onetwo.common.spring.web.mvc.JFishRequestMappingHandlerMapping;
import org.onetwo.common.spring.web.mvc.RequestMappingHandlerAdapterFactory;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
public class JFishDelegatingMvcConfig extends DelegatingWebMvcConfiguration {

	@Autowired
	private ApplicationContext applicationContex;
	
	public JFishDelegatingMvcConfig(){
	}

	public final void setConfigurerArray(WebMvcConfigurer... configurers) {
		if (LangUtils.isEmpty(configurers)) {
			return;
		}
		super.setConfigurers(Arrays.asList(configurers));
	}

	protected void addFormatters(FormatterRegistry registry) {
		registry.addConverterFactory(new JFishStringToEnumConverterFactory());
	}
	

	@Bean
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		JFishRequestMappingHandlerMapping handlerMapping = new JFishRequestMappingHandlerMapping();
		List<HandlerMappingListener> listeners = SpringUtils.getBeans(applicationContex, HandlerMappingListener.class);
		
		handlerMapping.setOrder(0);
		handlerMapping.setInterceptors(getInterceptors());
		handlerMapping.setPluginManager(JFishPluginManagerFactory.getPluginManager());
		handlerMapping.setListeners(listeners);
		
		return handlerMapping;
	}
	

	@Bean
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
		RequestMappingHandlerAdapterFactory af = SpringUtils.getBean(applicationContex, RequestMappingHandlerAdapterFactory.class);
		
		if(af!=null){
			ConfigurableWebBindingInitializer webBindingInitializer = new ConfigurableWebBindingInitializer();
			webBindingInitializer.setConversionService(mvcConversionService());
			webBindingInitializer.setValidator(mvcValidator());
			
			List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<HandlerMethodArgumentResolver>();
			addArgumentResolvers(argumentResolvers);

			List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<HandlerMethodReturnValueHandler>();
			addReturnValueHandlers(returnValueHandlers);
			
			RequestMappingHandlerAdapter adapter = af.createAdapter();
			adapter.setMessageConverters(getMessageConverters());
			adapter.setWebBindingInitializer(webBindingInitializer);
			adapter.setCustomArgumentResolvers(argumentResolvers);
			adapter.setCustomReturnValueHandlers(returnValueHandlers);
			return adapter;
			
		}else{
			return super.requestMappingHandlerAdapter();
		}
	}
	/*@Bean
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
		if(ClassUtils.isPresent("org.onetwo.plugins.rest.RestPlugin", getClass().getClassLoader())){
			ConfigurableWebBindingInitializer webBindingInitializer = new ConfigurableWebBindingInitializer();
			webBindingInitializer.setConversionService(mvcConversionService());
			webBindingInitializer.setValidator(mvcValidator());
			
			List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<HandlerMethodArgumentResolver>();
			addArgumentResolvers(argumentResolvers);

			List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<HandlerMethodReturnValueHandler>();
			addReturnValueHandlers(returnValueHandlers);
			
			RequestMappingHandlerAdapter adapter = new JFishRequestMappingHandlerAdapter();
			adapter.setMessageConverters(getMessageConverters());
			adapter.setWebBindingInitializer(webBindingInitializer);
			adapter.setCustomArgumentResolvers(argumentResolvers);
			adapter.setCustomReturnValueHandlers(returnValueHandlers);
			return adapter;
			
		}else{
			return super.requestMappingHandlerAdapter();
		}
	}*/
	

	/*@Override
	protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		super.configureMessageConverters(converters);
		this.addDefaultHttpMessageConverters(converters);
	}*/
}
