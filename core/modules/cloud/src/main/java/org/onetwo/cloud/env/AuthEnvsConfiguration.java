package org.onetwo.cloud.env;

import org.onetwo.boot.core.web.async.DelegateTaskDecorator.AsyncTaskDecorator;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.ImmutableSet;

/**
 * @author weishao zeng
 * <br/>
 */
@Configuration
@EnableConfigurationProperties(EnvProperties.class)
//@Import(RmqAuthEnvsConfiguration.class)
public class AuthEnvsConfiguration {
	@Autowired
	private EnvProperties envProperties;

	@Bean
	public AsyncTaskDecorator authEnvsTaskDecorator() {
		return new AuthEnvsTaskDecorator();
	}
	@Bean
	public AuthEnvs authEnvs(){
		AuthEnvs authEnvs = new AuthEnvs();
		if(!LangUtils.isEmpty(envProperties.getKeepHeaders())){
			authEnvs.setKeepHeaders(ImmutableSet.copyOf(envProperties.getKeepHeaders()));
		}
		return authEnvs;
	}
	
}
