package org.onetwo.cloud.zuul;

import org.onetwo.cloud.core.BootJfishCloudConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    
    @Bean
    @ConditionalOnClass(name="com.netflix.zuul.ZuulFilter")
    @ConditionalOnProperty(BootJfishCloudConfig.ZUUL_FIXHEADERS_ENABLED)
    public FixHeaderZuulFilter fixHeaderZuulFilter(){
    	FixHeaderZuulFilter filter = new FixHeaderZuulFilter();
    	filter.setFixHeaders(cloudConfig.getZuul().getFixHeaders());	
    	return filter;
    }
}
