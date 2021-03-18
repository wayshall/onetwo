package org.onetwo.boot.apiclient;

import java.util.Map;

import org.onetwo.boot.apiclient.ApiClientConfiguration.ApiClientProperties;
import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.common.apiclient.impl.RestExecutorConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.google.common.collect.Maps;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@EnableConfigurationProperties(ApiClientProperties.class)
public class ApiClientConfiguration {
//	@Autowired
//	private ApiClientProperties apiClientProperties;
	
//	@Bean
//	public RestExecutorConfig restExecutorConfig(){
//		return apiClientProperties;
//	}
	
	@Bean
	@ConditionalOnProperty(ApiClientProperties.PREFIX + ".headers")
	public ConfigHeaderApiInterceptor bearerTokenApiInterceptor() {
		return new ConfigHeaderApiInterceptor();
	}
	
	@ConfigurationProperties(ApiClientProperties.PREFIX)
	@Data
	@EqualsAndHashCode(callSuper=false)
	@Primary
	public static class ApiClientProperties extends RestExecutorConfig {
		/***
		 * jfish.apiclient
		 */
		public static final String PREFIX = BootJFishConfig.PREFIX + ".apiclient";
		
		/***
		 * jfish.apiclient.headers.Authorization=Bearer xxxx
		 */
		Map<String, String> headers = Maps.newHashMap();
	}
}
