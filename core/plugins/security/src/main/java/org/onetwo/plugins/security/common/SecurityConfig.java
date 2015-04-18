package org.onetwo.plugins.security.common;

import org.onetwo.common.fish.plugin.JFishPluginManager;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.plugins.security.SecurityPlugin;
import org.onetwo.plugins.security.utils.SecurityPluginUtils;

public class SecurityConfig extends JFishProperties {
	
	public static final String SERVER_LOGIN_URL = "server.login.url";
	public static final String SERVER_LOGOUT_URL = "server.logout.url";

	public boolean isSso(){
		return false;
	}
	
	/***
	 * 服务器登录地址
	 * @return
	 */
	public String getServerLoginUrl(){
		String url = getProperty(SERVER_LOGIN_URL);
		if(StringUtils.isBlank(url)){
			String name = SecurityPlugin.getInstance().getPluginName();
			url = JFishWebUtils.redirect("/" + name + "/common/login");
		}
		return url;
		
	}
	
	public String getServerLogoutUrl(){
		String url = getProperty(SERVER_LOGOUT_URL);
		if(StringUtils.isBlank(url)){
			String name = SecurityPlugin.getInstance().getPluginName();
			url = JFishWebUtils.redirect("/" + name + "/common/logout");
		}
		return url;
	}
	
	public String getAuthenticRedirectUrl(){
		String url = getProperty("authentic.redirect.url");
		if(StringUtils.isBlank(url)){
			if(isSso()){
				url = SecurityPluginUtils.SSO_CLIENT_LOGIN_URL;
			}else{
				url = getServerLoginUrl();
			}
		}
		return url;
	}
	
	public String getLoginView(){
		String name = SecurityPlugin.getInstance().getPluginName();
		return getProperty("login.view", JFishPluginManager.PLUGINNAME_PARSER.wrapViewPath(name, "login"));
	}
	
	/***
	 * 登录成功后跳转地址
	 * @return
	 */
	public String getLoginSuccessView(){
		return getProperty("login.success.view", JFishWebUtils.redirect("/index"));
	}
	
	public String getLogoutSuccessView(){
//		return getProperty("logout.success.view", SecurityPluginUtils.COMMON_LOGIN_URL);
		return getProperty("logout.success.view", getServerLoginUrl());
	}
}
