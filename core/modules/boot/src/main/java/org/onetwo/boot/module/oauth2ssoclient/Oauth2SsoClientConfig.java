package org.onetwo.boot.module.oauth2ssoclient;
/**
 * @author weishao zeng
 * <br/>
 */
public interface Oauth2SsoClientConfig {
	
	String getClientId();
	
	String getClientSecret();
	
	String getOauth2Scope();
	
	/***
	 * 默认传递到认证服务接口到回调跳转地址
	 * @author weishao zeng
	 * @return
	 */
	String getOauth2RedirectUri();
	
	/***
	 * 用户授权地址
	 * @author weishao zeng
	 * @return
	 */
	String getUserAuthorizationUri();
	
	/***
	 * @author weishao zeng
	 * @return
	 */
//	boolean isOauth2ErrorInBrowser();
	
	boolean isDebug();

}
