package org.onetwo.boot.module.oauth2ssoclient;
/**
 * @author weishao zeng
 * <br/>
 */
public class Oauth2SsoClients {

	public static final String SCOPE_SNSAPI_BASE = "snsapi_base";
	public static final String SCOPE_SNSAPI_USERINFO = "snsapi_userinfo";
	public static final String RESPONSE_TYPE_CODE = "code";
	
	public static final String STORE_USER_INFO_KEY = "ssoclient_oauth2_userInfo";
	public static final String STORE_STATE_KEY = "ssoclient_oauth2_state";
	
	public static final String PARAMS_STATE = "state";
	public static final String PARAMS_CODE = "code";
	

	public static final String AUTHORIZATION_CODE = "authorization_code";
	
	private Oauth2SsoClients() {
	}

}
