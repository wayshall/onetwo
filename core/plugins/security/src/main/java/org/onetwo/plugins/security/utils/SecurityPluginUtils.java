package org.onetwo.plugins.security.utils;

import org.onetwo.plugins.security.SecurityPlugin;

final public class SecurityPluginUtils {

	public static final String SSO_USERSERVICE_EXPORTER_NAME = "ssoUserServiceExporter";
	
	public static String getSsoUserServiceExporterDefaultUrl(String baseUrl){
		return baseUrl + SecurityPlugin.getInstance().getPluginMeta().getPluginInfo().getContextPath() + "/" + SSO_USERSERVICE_EXPORTER_NAME;
	}
	
	private SecurityPluginUtils(){
	}
}
