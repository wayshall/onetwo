package org.onetwo.boot.module.oauth2.ssoclient;

import org.onetwo.boot.module.oauth2.JFishOauth2Properties;
import org.onetwo.common.apiclient.impl.RestExecutorConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @see OAuth2ClientProperties security.oauth2.client.clientId
 * @author weishao zeng
 * <br/>
 */
@Data
@ConfigurationProperties(OAuth2SsoClientProperties.CONFIG_PREFIX)
public class OAuth2SsoClientProperties {

	/***
	 * jfish.oauth2.ssoclient
	 */
	public static final String CONFIG_PREFIX = JFishOauth2Properties.CONFIG_PREFIX + ".ssoclient";
	
	/***
	 * jfish.oauth2.ssoclient.tokenInfo.enabled
	 */
	public static final String ENABLED_TOKEN_INFO_CUSTOM = CONFIG_PREFIX + ".tokenInfo.enabled";
	
	RestExecutorConfig resttemplate = new RestExecutorConfig(60_000, 60_000, 60_000);
	
	/***
	 * 是否强制检查state参数。
	 * 
	 * 这里注意：
	 * 	本地测试，如果后端服务和前端分开时，两者使用不同的域名和端口，或者设置不同的context-path时，
	 * 因为DefaultOAuth2ClientContext是session scope的bean，设置state参数和使用state和code参数获取accessToken时其实是不同的会话（因为域名等不同导致了cookies不同），
	 * 即由于验证前和验证后重定向回来时无法保持同一个会话的bean，导致无法获取PreservedState，从而导致csrf异常。
	 * 所以如果前后端域名不同，应该设置stateMandatory=false并且不要传入state参数，避免csrf异常
	 */
	boolean stateMandatory = true;
	
	String loginPath = "/oauth2Login*";

}
