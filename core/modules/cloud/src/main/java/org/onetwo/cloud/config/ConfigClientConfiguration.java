package org.onetwo.cloud.config;

import static org.springframework.cloud.config.client.ConfigClientProperties.AUTHORIZATION;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.onetwo.boot.core.web.view.ExtJackson2HttpMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator.GenericRequestHeaderInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@ConditionalOnClass(name = "org.springframework.cloud.config.client.ConfigClientProperties")
public class ConfigClientConfiguration {

//	@ConditionalOnMissingBean(ConfigServicePropertySourceLocator.class)
	@Bean
	@ConditionalOnProperty(value = "spring.cloud.config.enabled", matchIfMissing = true)
	public ConfigServicePropertySourceLocator configServicePropertySource(ConfigClientProperties properties) {
		ConfigServicePropertySourceLocator locator = new ConfigServicePropertySourceLocator(properties);
		RestTemplate restTemplate = getSecureRestTemplate(properties);
		restTemplate.setMessageConverters(Arrays.asList(new ExtJackson2HttpMessageConverter()));
		locator.setRestTemplate(restTemplate);
		return locator;
	}

	/****
	 * copy form ConfigServicePropertySourceLocator#getSecureRestTemplate
	 * @author weishao zeng
	 * @param client
	 * @return
	 */
	private RestTemplate getSecureRestTemplate(ConfigClientProperties client) {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		if (client.getRequestReadTimeout() < 0) {
			throw new IllegalStateException("Invalid Value for Read Timeout set.");
		}
		requestFactory.setReadTimeout(client.getRequestReadTimeout());
		RestTemplate template = new RestTemplate(requestFactory);
		Map<String, String> headers = new HashMap<>(client.getHeaders());
		if (headers.containsKey(AUTHORIZATION)) {
			headers.remove(AUTHORIZATION); // To avoid redundant addition of header
		}
		if (!headers.isEmpty()) {
			template.setInterceptors(Arrays.<ClientHttpRequestInterceptor> asList(
					new GenericRequestHeaderInterceptor(headers)));
		}

		return template;
	}
}
