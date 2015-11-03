package org.onetwo.boot.plugins.security;

import javax.servlet.FilterConfig;

import lombok.Data;
import lombok.ToString;

import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.filter.WebContextConfigProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;


/***
 * @author way
 *
 */
@ConfigurationProperties(prefix="jfish.security")
@Data
@ToString
public class BootSecurityConfig implements WebContextConfigProvider {
	
	@Autowired
	private BootSiteConfig bootSiteConfig;
	private String logoutUrl;
	private CasConfig cas = new CasConfig();
//	private boolean csrf = true;
	
	private RememberMeConfig rememberMe = new RememberMeConfig();
	
	public String getUserLogoutUrl(){
		String url = logoutUrl;
		if(isCasEnabled()){
			url = cas.getLogoutUrl();
		}
		return bootSiteConfig.getBaseURL() + StringUtils.appendStartWithSlash(url);
	}
	
	public boolean isCasEnabled(){
		return SpringApplication.getInstance().containsClassBean("org.springframework.security.cas.web.CasAuthenticationFilter");
	}
	

	@Override
	public String getConfigName() {
		return "securityConfig";
	}

	@Override
	public Object getWebConfig(FilterConfig config) {
		return this;
	}

	@Data
	public class RememberMeConfig {
		private String key = "adf#$%^&*()_+)(*&^hr";
		private int tokenValiditySeconds = 60*60*24*30;
	}




	@Data
	public class CasConfig {
		private String loginUrl;
		private String logoutUrl;
		private String service;
		private boolean sendRenew = true;
		private String casServerUrl;
		private String key = CasConfig.class.getName();
	}
}
