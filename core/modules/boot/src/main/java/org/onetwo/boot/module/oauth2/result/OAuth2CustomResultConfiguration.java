package org.onetwo.boot.module.oauth2.result;

import org.onetwo.boot.core.json.ObjectMapperProvider;
import org.onetwo.boot.core.web.view.XResponseViewManager;
import org.onetwo.boot.module.oauth2.result.OAuth2ExceptionDataResultJsonSerializer.OAuth2ExceptionMixin;
import org.onetwo.common.web.utils.RequestUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 配置定制的OAuth2ExceptionRenderer，返回框架约定的的格式
 * @author wayshall
 * <br/>
 */
@Configuration
public class OAuth2CustomResultConfiguration implements InitializingBean{

	@Autowired(required=false)
	private XResponseViewManager xresponseViewManager;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(xresponseViewManager!=null){
			xresponseViewManager.registerMatchPredicate(body->{
				return RequestUtils.getCurrentServletPath().map(path->path.contains("/oauth/")).orElse(false);
			});
		}
	}
	@Bean
	public ObjectMapperProvider objectMapperProvider(){
		return ()->{
			ObjectMapper jsonMapper = ObjectMapperProvider.DEFAULT.createObjectMapper();
			jsonMapper.addMixIn(OAuth2Exception.class, OAuth2ExceptionMixin.class);
			return jsonMapper;
		};
	}
	@Bean
	public DataResultOAuth2ExceptionRenderer oauth2ExceptionRenderer(){
		return new DataResultOAuth2ExceptionRenderer(objectMapperProvider());
	}
	
	@Bean
	public OAuth2AuthenticationEntryPoint oauth2AuthenticationEntryPoint(){
		OAuth2AuthenticationEntryPoint ep = new OAuth2AuthenticationEntryPoint();
		ep.setExceptionRenderer(oauth2ExceptionRenderer());
		return ep;
	}
	
	@Bean
	public OAuth2AccessDeniedHandler oauth2AccessDeniedHandler(){
		OAuth2AccessDeniedHandler dh = new OAuth2AccessDeniedHandler();
		dh.setExceptionRenderer(oauth2ExceptionRenderer());
		return dh;
	}
	
}
