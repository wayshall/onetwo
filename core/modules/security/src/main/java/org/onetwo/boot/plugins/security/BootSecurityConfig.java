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
	public static final String LOGIN_PATH = "/login";
	public static final String LOGIN_PROCESS_PATH = "/dologin";
	public static final String LOGOUT_PATH = "/logout";
	public static final String TARGET_PATH_AFTER_LOGIN = "/plugins/permission/admin";
	
	@Autowired
	private BootSiteConfig bootSiteConfig;
	private String logoutUrl = LOGOUT_PATH;
	private String loginUrl = LOGIN_PATH;
	private String loginProcessUrl = LOGIN_PROCESS_PATH;
	private String afterLoginUrl = TARGET_PATH_AFTER_LOGIN;
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
		private String key = "@#%AS%&DF_=PJ}{EB23+42342*()*^%$)_(*%^)";
		/**
		 * 30 days
		 */
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
