package org.onetwo.ext.security.utils;

import lombok.Data;
import lombok.ToString;

import org.onetwo.common.spring.SpringApplication;

import redis.clients.jedis.JedisPoolConfig;


/***
 * @author way
 *
 */
@Data
@ToString
public class SecurityConfig {
	public static final String LOGIN_PATH = "/login";
	public static final String LOGIN_PROCESS_PATH = "/dologin";
	public static final String LOGOUT_PATH = "/logout";
	public static final String TARGET_PATH_AFTER_LOGIN = "/admin";
	
	private String logoutUrl = LOGOUT_PATH;
	private String loginUrl = LOGIN_PATH;
	private String loginProcessUrl = LOGIN_PROCESS_PATH;
	private String afterLoginUrl = TARGET_PATH_AFTER_LOGIN;
	private CasConfig cas = new CasConfig();
//	private boolean csrf = true;
	protected Boolean syncPermissionData;
	
	private RememberMeConfig rememberMe = new RememberMeConfig();
	
	private RedisConfig redis = new RedisConfig();
	private CookieConfig cookie = new CookieConfig();
	
	public String getUserLogoutUrl(){
		String url = logoutUrl;
		if(isCasEnabled()){
			url = cas.getLogoutUrl();
		}
		return url;
	}
	
	public void setAfterLoginUrl(String afterLoginUrl){
		this.afterLoginUrl = afterLoginUrl;
	}
	public boolean isCasEnabled(){
		return SpringApplication.getInstance().containsClassBean("org.springframework.security.cas.web.CasAuthenticationFilter");
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

	@Data
	public static class RedisConfig {
		private String hostName = "localhost";
		private int port = 6379;
		private JedisPoolConfig pool;
	}

	@Data
	public static class CookieConfig {
		private String path = "/";
		private String domain;
		private String name = "sid";
	}
}
