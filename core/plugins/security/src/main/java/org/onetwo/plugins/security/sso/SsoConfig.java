package org.onetwo.plugins.security.sso;

import org.onetwo.plugins.security.common.SecurityConfig;
import org.onetwo.plugins.security.utils.SecurityPluginUtils;

abstract public class SsoConfig extends SecurityConfig {

	abstract public boolean isSsoServer();
	abstract public boolean isSsoClient();
	public boolean isSso(){
		return true;
	}
	
	
	/****
	 * 是否使用跨域session
	 * @return
	 */
	public boolean isCrossdomainSession(){
		return getBoolean("crossdomain.session", false);
	}
	

	/*public String getReturnUrl(){
		return getAndThrowIfEmpty("return.url");
	}*/

	public String getSignKey(){
		return getProperty("sgin.key", SecurityPluginUtils.DEFAULT_SSO_SIGN_KEY);
	}
	
	public synchronized Object get(Object key) {
		if("serverLoginUrl".equals(key)){
			return getServerLoginUrl();
		}else if("serverLogoutUrl".equals(key)){
			return getServerLogoutUrl();
		}
		return super.get(key);
	}
	
	/****
	 * sso服务器登录地址
	 * @return
	 */
	abstract public String getServerLoginUrl();
	
	abstract public String getServerLogoutUrl();
}
