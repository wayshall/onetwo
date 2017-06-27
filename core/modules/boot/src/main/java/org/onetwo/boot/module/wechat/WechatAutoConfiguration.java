package org.onetwo.boot.module.wechat;

import org.onetwo.boot.core.web.mvc.interceptor.MvcInterceptor;
import org.onetwo.boot.core.web.mvc.interceptor.MvcInterceptorManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@ConditionalOnClass(WechatOAuth2MvcInterceptor.class)
public class WechatAutoConfiguration {

	
	@Bean
	@ConditionalOnBean(MvcInterceptorManager.class)
	public MvcInterceptor wechatOAuth2MvcInterceptor(){
		return new WechatOAuth2MvcInterceptor();
	}
}
