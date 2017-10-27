package org.onetwo.boot.module.oauth2;

import java.util.Map.Entry;

import org.onetwo.boot.module.oauth2.JFishOauth2Properties.ResourceServerProps;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.security.method.DefaultMethodSecurityConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@EnableResourceServer
@EnableConfigurationProperties(JFishOauth2Properties.class)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
	
	@Autowired
	private JFishOauth2Properties oauth2Properties;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		ResourceServerProps resourceServerProps = oauth2Properties.getResourceServer();
		if(!LangUtils.isEmpty(resourceServerProps.getRequestMatchers())){
			http.requestMatchers()
				.antMatchers(resourceServerProps.getRequestMatchers());
		}
		for(Entry<String[], String> entry : resourceServerProps.getIntercepterUrls().entrySet()){
			http.authorizeRequests().antMatchers(entry.getKey()).access(entry.getValue());
		}
		DefaultMethodSecurityConfigurer.defaultAnyRequest(http, resourceServerProps.getAnyRequest());
	}
	
}
