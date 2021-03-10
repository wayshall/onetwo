package org.onetwo.boot.module.oauth2ssoclient;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
@ConfigurationProperties(OAuth2SsoClientProperties.CONFIG_PREFIX)
public class OAuth2SsoClientProperties implements Oauth2SsoClientConfig {

	public static final String CONFIG_PREFIX = BootJFishConfig.PREFIX + ".oauth2ssoclient";
	
	String clientId;
	
	String clientSecret;
	
	String oauth2Scope;
	
	/***
	 * 默认传递到认证服务接口到回调跳转地址
	 * @author weishao zeng
	 * @return
	 */
	String oauth2RedirectUri;
	
	/***
	 * 用户授权地址
	 * @author weishao zeng
	 * @return
	 */
	String userAuthorizationUri;
	
	/***
	 * @author weishao zeng
	 * @return
	 */
	boolean debug;
	
}
