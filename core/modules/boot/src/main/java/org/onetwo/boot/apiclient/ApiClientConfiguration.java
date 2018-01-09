package org.onetwo.boot.apiclient;

import org.onetwo.boot.apiclient.ApiClientConfiguration.ApiClientProperties;
import org.onetwo.common.apiclient.impl.RestExecutorConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@EnableConfigurationProperties(ApiClientProperties.class)
public class ApiClientConfiguration {
	@Autowired
	private ApiClientProperties apiClientProperties;
	
	@Bean
	public RestExecutorConfig restExecutorConfig(){
		return apiClientProperties;
	}
	
	@ConfigurationProperties("jfish.apiclient")
	public static class ApiClientProperties extends RestExecutorConfig {
	}
}
