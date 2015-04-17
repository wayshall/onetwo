package org.onetwo.plugins.security.common;

import org.onetwo.common.fish.plugin.JFishPluginManager;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.onetwo.plugins.security.SecurityPlugin;
import org.onetwo.plugins.security.utils.SecurityPluginUtils;

public class SecurityConfig extends JFishProperties {

	public boolean isSso(){
		return false;
	}
	
	public String getLoginView(){
		String name = SecurityPlugin.getInstance().getPluginName();
		return getProperty("login.view", JFishPluginManager.PLUGINNAME_PARSER.wrapViewPath(name, "login"));
	}
	
	public String getLoginSuccessView(){
		return getProperty("login.success.view", JFishWebUtils.redirect("/index"));
	}
	
	public String getLogoutSuccessView(){
		return getProperty("logout.success.view", SecurityPluginUtils.COMMON_LOGIN_URL);
	}
}
