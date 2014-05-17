package org.onetwo.plugins.security.common;

import org.onetwo.common.utils.propconf.JFishProperties;
import org.onetwo.plugins.security.utils.SecurityPluginUtils;

abstract public class SsoConfig extends JFishProperties {

	abstract public boolean isServerSide();
	abstract public boolean isClientSide();
	

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
