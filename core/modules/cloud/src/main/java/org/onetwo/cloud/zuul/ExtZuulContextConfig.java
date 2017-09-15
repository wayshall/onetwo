package org.onetwo.cloud.zuul;

import org.onetwo.boot.core.web.mvc.BootStandardServletMultipartResolver;
import org.onetwo.cloud.core.BootJfishCloudConfig;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.spring.filter.SpringMultipartFilterProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.MultipartProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;

/**
 * @author wayshall
 * <br/>
 */
@EnableDiscoveryClient
@EnableConfigurationProperties({BootJfishCloudConfig.class})
@Configuration
public class ExtZuulContextConfig {
	@Autowired
    private BootJfishCloudConfig cloudConfig;
	@Autowired
	private MultipartProperties multipartProperties;
    
    @Bean
    @ConditionalOnClass(name="com.netflix.zuul.ZuulFilter")
    @ConditionalOnProperty(BootJfishCloudConfig.ZUUL_FIXHEADERS_ENABLED)
    public FixHeaderZuulFilter fixHeaderZuulFilter(){
    	FixHeaderZuulFilter filter = new FixHeaderZuulFilter();
    	filter.setFixHeaders(cloudConfig.getZuul().getFixHeaders());	
    	return filter;
    }
    
    @Bean
    public SpringMultipartFilterProxy springMultipartFilterProxy(){
    	return new SpringMultipartFilterProxy();
    }
    
	@Bean(name=MultipartFilter.DEFAULT_MULTIPART_RESOLVER_BEAN_NAME)
	public MultipartResolver filterMultipartResolver(){
		BootStandardServletMultipartResolver resolver = new BootStandardServletMultipartResolver();
		resolver.setMaxUploadSize(FileUtils.parseSize(multipartProperties.getMaxRequestSize()));
		return resolver;
	}
}
