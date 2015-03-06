package org.onetwo.plugins.security.server;

import java.util.Collection;
import java.util.Map;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.plugins.security.common.SsoConfig;
import org.onetwo.plugins.security.utils.SecurityPluginUtils;

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

	public String getServerLoginUrl(){
		String url = getProperty("server.login.url");
		if(StringUtils.isBlank(url)){
			url = BaseSiteConfig.getInstance().getBaseURL()+"/login";
		}
		return url;
	}

	public String getServerLogoutUrl(){
		String url = getProperty("server.logout.url");
		if(StringUtils.isBlank(url)){
			url = BaseSiteConfig.getInstance().getBaseURL()+"/logout";
		}
		return url;
	}
	
	public long getTimeout(){
		return getLong("session.timeout", 60L);
	}
	
	/***
	 * 获取客户端设置cookies的地址
	 * @param site
	 * @return
	 */
	public String getClientLoginUrl(String site){
		String url = getProperty("client."+site+".login.url");
		if(StringUtils.isBlank(url)){
			url = getClientUrl(site);
			url = SecurityPluginUtils.getClientLoginUrl(url);
		}
		return url;
	}
	public String getClientLogoutUrl(String site){
		String url = getProperty("client."+site+".logout.url");
		if(StringUtils.isBlank(url)){
			url = getClientUrl(site);
			url = SecurityPluginUtils.getClientLogoutUrl(url);
		}
		return url;
	}

	public String getClientUrl(String site){
		Map<String, String> clients = getClients();
		String url = clients.get(site);
		return url;
	}
	
	public Collection<String> getClientUrls(){
		return getPropertiesStartWith("client.").values();
	}
	
	public Collection<String> getClientNames(){
		return getPropertiesStartWith("client.").keySet();
	}
	
	public Map<String, String> getClients(){
		return getPropertiesStartWith("client.");
	}
	
}
