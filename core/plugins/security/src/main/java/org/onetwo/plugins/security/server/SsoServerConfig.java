package org.onetwo.plugins.security.server;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.plugins.security.common.SsoConfig;

public class SsoServerConfig extends SsoConfig {


	public boolean isServerSide(){
		return true;
	}
	public boolean isClientSide(){
		return false;
	}
	
	public String getService(String name){
		return getProperty("services."+name);
	}

	public String getLoginUrl(){
		String url = getProperty("login.url");
		if(StringUtils.isBlank(url)){
			url = BaseSiteConfig.getInstance().getBaseURL()+"/login";
		}
		return url;
	}
}
