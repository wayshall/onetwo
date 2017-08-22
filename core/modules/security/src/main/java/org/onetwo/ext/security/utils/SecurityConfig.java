package org.onetwo.ext.security.utils;

import java.util.concurrent.TimeUnit;

import lombok.Data;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.spring.Springs;
import org.onetwo.ext.security.jwt.JwtSecurityUtils;

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
	/***
	 * LogoutConfigurer#logoutSuccessUrl default value
	 */
	private String logoutSuccessUrl = "/login?logout";
	private String loginProcessUrl = LOGIN_PROCESS_PATH;
	private String afterLoginUrl = TARGET_PATH_AFTER_LOGIN;
	
	private boolean alwaysUseDefaultTargetUrl = true;

	//AccessDenied redirectErrorUrl
	private String redirectErrorUrl;
	//AccessDenied errorPage
	private String errorPage = "/error";
	
	private boolean forceHttps;
	private Integer httpsPort;
	
	private CasConfig cas = new CasConfig();
	private CrsfConfig csrf = new CrsfConfig();
//	private boolean csrf = true;
	protected Boolean syncPermissionData;
	
	private RememberMeConfig rememberMe = new RememberMeConfig();
	
	private RedisConfig redis = new RedisConfig();
	private CookieConfig cookie = new CookieConfig();
	private RbacConfig rbac = new RbacConfig();
	private JwtConfig jwt = new JwtConfig();
	
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
		return Springs.getInstance().containsClassBean("org.springframework.security.cas.web.CasAuthenticationFilter");
	}
	
	@Data
	public class RememberMeConfig {
		private String key;
		/**
		 * 30 days
		 */
		private int tokenValiditySeconds = 60*60*24*7;
		
		public boolean isEnabled(){
			return StringUtils.isNotBlank(key);
		}
	}



	/****
	 * cas:
            service: http://localhost:8080/j_spring_cas_security_check
            loginUrl: https://sso.server.com:9443/cas-server/login
            casServerUrl: https://sso.server.com:9443/cas-server
            logoutUrl: https://sso.server.com:9443/cas-server/logout
	 * @author way
	 *
	 */
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

	@Data
	public static class CrsfConfig {
		private boolean disable;
		private String[] ignoringPaths;
		private String[] requirePaths;
	}

	@Data
	public static class RbacConfig {
		private String resourceQuery;
	}
	
	@Data
	public static class JwtConfig {
		String authHeader = JwtSecurityUtils.DEFAULT_HEADER_KEY;
		String signingKey;
		Long expirationInSeconds = TimeUnit.HOURS.toSeconds(1);
		
		public boolean isEnabled(){
			return StringUtils.isNotBlank(signingKey);
		}
	}
}
