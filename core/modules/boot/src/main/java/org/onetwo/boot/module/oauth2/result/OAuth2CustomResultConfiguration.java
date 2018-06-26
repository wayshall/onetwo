package org.onetwo.boot.module.oauth2.result;

import org.onetwo.boot.core.json.ObjectMapperProvider;
import org.onetwo.boot.core.json.ObjectMapperProvider.DefaultObjectMapperProvider;
import org.onetwo.boot.core.json.ObjectMapperProvider.ObjectMapperCustomizer;
import org.onetwo.boot.core.web.view.XResponseViewManager;
import org.onetwo.boot.module.oauth2.result.OAuth2ExceptionDataResultJsonSerializer.OAuth2ExceptionMixin;
import org.onetwo.common.web.utils.RequestUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 配置定制的OAuth2ExceptionRenderer，返回框架约定的的格式
 * @author wayshall
 * <br/>
 */
@ConditionalOnBean(TokenStore.class)
@Configuration
public class OAuth2CustomResultConfiguration implements InitializingBean{

	@Autowired(required=false)
	private XResponseViewManager xresponseViewManager;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(xresponseViewManager!=null){
			xresponseViewManager.registerMatchPredicate(body->{
				if(OAuth2Exception.class.isInstance(body)){
					return false;
				}
				return RequestUtils.getCurrentServletPath().map(path->path.contains("/oauth/")).orElse(false);
			}, new OAuth2DataResultWrapper());
		}
	}

	@Bean
	@ConditionalOnMissingBean(ObjectMapperProvider.class)
	public ObjectMapperProvider objectMapperProvider(){
		return new DefaultObjectMapperProvider();
	}
	
	@Bean
	public ObjectMapperCustomizer oauth2ObjectMapperCustomizer(){
		return jsonMapper->{
			jsonMapper.addMixIn(OAuth2Exception.class, OAuth2ExceptionMixin.class);
		};
	}
	@Bean
	public DataResultOAuth2ExceptionRenderer oauth2ExceptionRenderer(ObjectMapperProvider objectMapperProvider){
		return new DataResultOAuth2ExceptionRenderer(objectMapperProvider);
	}
	
	@Bean
	public OAuth2AuthenticationEntryPoint oauth2AuthenticationEntryPoint(ObjectMapperProvider objectMapperProvider){
		OAuth2AuthenticationEntryPoint ep = new OAuth2CustomAuthenticationEntryPoint();
		ep.setExceptionRenderer(oauth2ExceptionRenderer(objectMapperProvider));
		return ep;
	}
	
	@Bean
	public OAuth2AccessDeniedHandler oauth2AccessDeniedHandler(ObjectMapperProvider objectMapperProvider){
		OAuth2AccessDeniedHandler dh = new OAuth2AccessDeniedHandler();
		dh.setExceptionRenderer(oauth2ExceptionRenderer(objectMapperProvider));
		return dh;
	}
	
}
