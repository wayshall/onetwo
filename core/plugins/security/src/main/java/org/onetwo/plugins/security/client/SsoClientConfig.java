package org.onetwo.plugins.security.client;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.onetwo.plugins.security.utils.SecurityPluginUtils;

public class SsoClientConfig extends JFishProperties {

	public String getServerUrl(){
		return getAndThrowIfEmpty("server.url");
	}
	public String getSSOUserServiceUrl(){
		String url = getProperty("sso.service.url");
		if(StringUtils.isBlank(url)){
			url = SecurityPluginUtils.getSsoUserServiceExporterDefaultUrl(getServerUrl());
		}
		return url;
	}
}
