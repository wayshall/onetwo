package org.onetwo.plugins.security.client;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.security.common.SsoConfig;
import org.onetwo.plugins.security.utils.SecurityPluginUtils;

public class SsoClientConfig extends SsoConfig {

	@Override
	public boolean isServerSide() {
		return false;
	}
	@Override
	public boolean isClientSide() {
		return true;
	}
	public String getServerUrl(){
		return getAndThrowIfEmpty("server.url");
	}
	public String getLoginUrl(){
		String url = getProperty("login.url");
		if(StringUtils.isBlank(url)){
			url = getServerUrl()+"/login";
		}
		return url;
	}
	public String getLogoutUrl(){
		String url = getProperty("logout.url");
		if(StringUtils.isBlank(url)){
			url = getServerUrl()+"/logout";
		}
		return url;
	}
	public String getSSOUserServiceUrl(){
		String url = getProperty("service.url");
		if(StringUtils.isBlank(url)){
			url = SecurityPluginUtils.getSsoUserServiceExporterDefaultUrl(getServerUrl());
		}
		return url;
	}
}
