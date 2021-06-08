package org.onetwo.cloud.zuul;

import org.onetwo.boot.bugfix.AllEncompassingFormHttpMessageConverter;
import org.onetwo.boot.core.embedded.BootServletContainerCustomizer;
import org.onetwo.boot.core.embedded.TomcatProperties;
import org.onetwo.boot.core.web.filter.SpringMultipartFilterProxy;
import org.onetwo.boot.core.web.mvc.BootStandardServletMultipartResolver;
import org.onetwo.cloud.bugfix.FixFormBodyWrapperFilterPostProcessor;
import org.onetwo.cloud.core.BootJfishCloudConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.filter.OrderedHiddenHttpMethodFilter;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;

/**
 * @author wayshall
 * <br/>
 */
@EnableDiscoveryClient
@EnableConfigurationProperties({BootJfishCloudConfig.class, TomcatProperties.class})
@Configuration
public class ExtZuulContextConfig {
	@Autowired
	private MultipartProperties multipartProperties;

	@Bean
	@ConditionalOnProperty(value=TomcatProperties.ENABLED_CUSTOMIZER_TOMCAT, matchIfMissing=true, havingValue="true")
	@ConditionalOnMissingBean(BootServletContainerCustomizer.class)
	public BootServletContainerCustomizer bootServletContainerCustomizer(){
		return new BootServletContainerCustomizer();
	}
	
    @Bean
    @ConditionalOnClass(name="com.netflix.zuul.ZuulFilter")
    @ConditionalOnProperty(BootJfishCloudConfig.ZUUL_FIXHEADERS_ENABLED)
    @RefreshScope
    public FixHeaderZuulFilter fixHeaderZuulFilter(){
    	FixHeaderZuulFilter filter = new FixHeaderZuulFilter();
//    	filter.setFixHeaders(cloudConfig.getZuul().getFixHeaders());	
    	return filter;
    }
    
//    @Bean
//    public WebsocketZuulFilter websocketZuulFilter() {
//    	return new WebsocketZuulFilter();
//    }
    
    @Bean
    @ConditionalOnMissingBean(SpringMultipartFilterProxy.class)
    public SpringMultipartFilterProxy springMultipartFilterProxy(){
    	return new SpringMultipartFilterProxy();
    }
    
	@Bean(name=MultipartFilter.DEFAULT_MULTIPART_RESOLVER_BEAN_NAME)
	@ConditionalOnMissingBean(name={MultipartFilter.DEFAULT_MULTIPART_RESOLVER_BEAN_NAME})
	public MultipartResolver filterMultipartResolver(){
		BootStandardServletMultipartResolver resolver = new BootStandardServletMultipartResolver();
		resolver.setMaxUploadSize(multipartProperties.getMaxRequestSize().toBytes());
		return resolver;
	}
	
	@Bean
	public static FixFormBodyWrapperFilterPostProcessor formBodyWrapperFilterPostProcessor(){
		return new FixFormBodyWrapperFilterPostProcessor();
	}
	
	/****
	 * 修复put方法的参数值里有%号的时，抛异常 Incomplete trailing escape (%) pattern的问题
	 * 
	 * @author weishao zeng
	 * @return
	 */
	@Bean
	public HttpPutFormContentFilter httpPutFormContentFilter() {
		HttpPutFormContentFilter filter = new HttpPutFormContentFilter();
		filter.setFormConverter(new AllEncompassingFormHttpMessageConverter());
		return new HttpPutFormContentFilter();
	}
	
	/*@Bean
	public RefreshEurekaServersListener refreshEurekaServersListener() {
		return new RefreshEurekaServersListener();
	}*/


	/*@Bean
	public FormBodyWrapperFilter formBodyWrapperFilter() {
		AllEncompassingFormHttpMessageConverter converter = new AllEncompassingFormHttpMessageConverter();
		return new FormBodyWrapperFilter(converter);
	}*/
	
	/*@Bean
	public FormBodyWrapperFilter formBodyWrapperFilter(){
		return new FixFormBodyWrapperFilter();
	}*/
	@Bean
	@ConditionalOnMissingBean(HiddenHttpMethodFilter.class)
	public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter() {
		OrderedHiddenHttpMethodFilter filter = new OrderedHiddenHttpMethodFilter();
		filter.setMethodParam("_disable_spring_mvc_hidden_method_for_fucking_zuul");
		return filter;
	}
}
