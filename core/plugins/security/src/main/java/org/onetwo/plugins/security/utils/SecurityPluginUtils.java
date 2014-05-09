package org.onetwo.plugins.security.utils;

import org.onetwo.plugins.security.SecurityPlugin;

final public class SecurityPluginUtils {

	public static final String LOGIN_PARAM_CLIENT_CODE = "clientCode";
//	public static final String DEFAULT_SSO_SIGN_KEY = "asdfa7sd9fa[ko@#$s0df]pips9";
	public static final String SSO_USERSERVICE_EXPORTER_NAME = "ssoUserServiceExporter";
	
	public static String getSsoUserServiceExporterDefaultUrl(String baseUrl){
		return baseUrl + SecurityPlugin.getInstance().getPluginMeta().getPluginInfo().getContextPath() + "/" + SSO_USERSERVICE_EXPORTER_NAME;
	}
	
	public static String getClientLoginUrl(String baseUrl){
		return baseUrl + SecurityPlugin.getInstance().getPluginMeta().getPluginInfo().getContextPath() + "/client/ssologin";
	}
	
	public static String getClientLogoutUrl(String baseUrl){
		return baseUrl + SecurityPlugin.getInstance().getPluginMeta().getPluginInfo().getContextPath() + "/client/ssologout";
	}
	
	private SecurityPluginUtils(){
	}
}
