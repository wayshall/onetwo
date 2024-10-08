package org.onetwo.ext.security.utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.propconf.JFishProperties;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.utils.LangOps;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.ext.security.jwt.JwtAuthStores;
import org.onetwo.ext.security.jwt.JwtSecurityUtils;
import org.onetwo.ext.security.method.DefaultMethodSecurityConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import redis.clients.jedis.JedisPoolConfig;


/***
 * @author way
 *
 */
@Data
@ToString
public class SecurityConfig {
	/***
	 * 不做任何配置
	 */
	public static final String ANY_REQUEST_NONE = "none";
	
	public static final String LOGIN_PATH = "/login";
	
	public static final String LOGIN_PROCESS_PATH = "/dologin";
	public static final String LOGOUT_PATH = "/logout";
	public static final String TARGET_PATH_AFTER_LOGIN = "/admin";
	
	//预留，未使用
	private String urlPrefix = "";
	String defaultLoginPage;
	private String logoutUrl = LOGOUT_PATH;
	private String loginUrl = LOGIN_PATH;
	/***
	 * 当认证失败（抛出AuthenticationException异常）时，在转发到 failureUrl 时，是否把异常信息附加到message参数
	 */
	private boolean failureUrlWithMessage = true;
	private String failureUrl;
	/***
	 * LogoutConfigurer#logoutSuccessUrl default value
	 */
	private String logoutSuccessUrl;
	private String loginProcessUrl = LOGIN_PROCESS_PATH;
	private String afterLoginUrl = TARGET_PATH_AFTER_LOGIN;
	
	private boolean alwaysUseDefaultTargetUrl = true;

	//AccessDenied redirectErrorUrl
//	private String redirectErrorUrl;
	//AccessDenied errorPage
	private String errorPage = "/error";
	
	private RedirectStrategyConfig redirectStrategy = new RedirectStrategyConfig();
	
	private CasConfig cas = new CasConfig();
	private CrsfConfig csrf = new CrsfConfig();
//	private boolean csrf = true;
	/***
	 * 是否在启动的时候同步菜单类的数据到数据库
	 */
//	protected boolean syncPermissionData;
	private PermConfig permission = new PermConfig();
	
	private RememberMeConfig rememberMe = new RememberMeConfig();
	
	private RedisConfig redis = new RedisConfig();
	private CookieConfig cookie = new CookieConfig();
	private RbacConfig rbac = new RbacConfig();
	private JwtConfig jwt = new JwtConfig();
	
	private Map<String[], String> intercepterUrls = Maps.newHashMap();
	private List<InterceptersConfig> intercepters = Lists.newArrayList();
	/***
	 * 是否检查：/** -> permitAll
	 */
	private boolean checkAnyUrlpermitAll = true;
	
	private String anyRequest = SecurityConfig.ANY_REQUEST_NONE;
	private boolean ignoringDefautStaticPaths = true;
	private String[] ignoringUrls;
	
	private Map<String, MemoryUser> memoryUsers = Maps.newHashMap();
	
	private boolean debug;
	
	private CorsConfig cors = new CorsConfig();
	
	/***
	 * 是否拒绝公共调用
	 * 当访问的方法或请求没有配置权限(即公共请求)时，是否拒绝访问
	 */
	private boolean rejectPublicInvocations = true;
	
	private boolean logSecurityError;
	
	/****
	 * 是否验证所有权限
	 */
	private boolean validateConfigAttributes = true;
	
	private StrictHttpFirewallConfig strictHttpFirewall = new StrictHttpFirewallConfig();
	/***
	 * 会话
	 */
	private SessionConfig session = new SessionConfig();
	
	/***
	 * 当security当Authentication为AnonymousAuthenticationToken时，是否直接拒绝，抛出AccessDeniedCodeException异常
	 * 默认为false
	 */
	boolean denyAnonymousUser;
	
	/***
	 * @see org.onetwo.ext.security.MultiExpressionVoter
	 * @return
	 */
	public boolean isDenyAnonymousUser() {
		return denyAnonymousUser;
	}
	
	public boolean isDebug(){
		return debug;
	}
	
	public boolean isLogSecurityError() {
		return logSecurityError;
	}
	
	public void setSyncPermissionData(boolean syncPermissionData) {
		this.permission.setSync2db(syncPermissionData);
	}
	
	public String[] getIgnoringUrls(){
		return ignoringUrls;
	}
	public String getAnyRequest(){
		return anyRequest;
	}
	/**
	 * for page
	 * @author wayshall
	 * @return
	 */
	public String getUserLogoutUrl(){
		String url = logoutUrl;
		/*if(isCasEnabled()){
			url = cas.getLogoutUrl();
		}*/
		return resolveUrl(url);
	}
	
	public String getDefaultLoginPage(){
		return defaultLoginPage;
	}
	
	/****
	 * @see DefaultMethodSecurityConfigurer#defaultConfigure
	 * @author wayshall
	 * @return
	 */
	public String getLoginUrl(){
		return resolveUrl(loginUrl);
	}
	
	/****
	 * 没有配置，则默认为loginUrl
	 * @author weishao zeng
	 * @return
	 */
	public String getFailureUrl(){
		String failureUrl = this.failureUrl;
		if(StringUtils.isBlank(failureUrl)){
			failureUrl = getLoginUrl() + "?error=true";
			return failureUrl;
		}else{
			return resolveUrl(failureUrl);
		}
	}
	/***
	 * 
	 * @see DefaultMethodSecurityConfigurer#defaultConfigure
	 * @author wayshall
	 * @return
	 */
	public String getLogoutUrl(){
		return resolveUrl(logoutUrl);
	}
	
	public String getAfterLoginUrl(){
		return resolveUrl(afterLoginUrl);
	}
	
	public String getLoginProcessUrl(){
		return resolveUrl(loginProcessUrl);
	}
	
	public String getLogoutSuccessUrl(){
		if(StringUtils.isBlank(logoutSuccessUrl)){
			return RequestUtils.appendParamString(getLoginUrl(), "logout");
		}
		return resolveUrl(logoutSuccessUrl);
	}
	
	private String resolveUrl(String url){
		return urlPrefix + url;
	}
	
	/*public String getRedirectErrorUrl(){
		return redirectErrorUrl;
	}*/
	
	public String getErrorPage(){
		return errorPage;
	}
	
	public void setAfterLoginUrl(String afterLoginUrl){
		this.afterLoginUrl = afterLoginUrl;
	}
	public boolean isCasEnabled(){
		return Springs.getInstance().containsClassBean("org.springframework.security.cas.web.CasAuthenticationFilter");
	}
	
	public Map<String, MemoryUser> getMemoryUsers(){
		return this.memoryUsers;
	}
	
	public boolean isIgnoringDefautStaticPaths(){
		return ignoringDefautStaticPaths;
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

	@Data
	public static class PermConfig {
		/***
		 * 是否在启动的时候同步菜单类的数据到数据库
		 */
		private boolean sync2db;
		private ControllerPermissionNotFoundActions permissionNotFound = ControllerPermissionNotFoundActions.THROWS;
		
		public void setSync2db(boolean sync2db) {
			this.sync2db = sync2db;
		}
	}
	public static enum ControllerPermissionNotFoundActions {
		/***
		 * 忽略
		 */
		IGNORE,
		/***
		 * 抛错
		 */
		THROWS
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

	@SuppressWarnings("serial")
	@EqualsAndHashCode(callSuper=false)
	@Data
	public static class RedisConfig extends JFishProperties {
		public RedisConfig() {
			super();
			this.setProperty("hostName", "localhost");
			this.setProperty("port", "6379");
		}
		private JedisPoolConfig pool;

		public String getHostName() {
			return getProperty("hostName");
		}
		public int getPort() {
			return getInt("port");
		}
		
	}

	@Data
	public static class CookieConfig {
		private String path;
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
		String issuer = "jfish";
		String audience = "webclient";
		
		String authHeader = JwtSecurityUtils.DEFAULT_HEADER_KEY;
		String authKey = JwtSecurityUtils.DEFAULT_HEADER_KEY;
		JwtAuthStores authStore = JwtAuthStores.HEADER;
		String signingKey;
		Long expirationInSeconds = TimeUnit.HOURS.toSeconds(1);
		String expiration;
		Boolean enabled;
		
		public boolean isEnabled(){
			if (enabled!=null) {
				return enabled;
			}
			return StringUtils.isNotBlank(signingKey);
		}

		public Long getExpirationInSeconds() {
			if(StringUtils.isNotBlank(expiration)){
				long inSeconds = LangOps.timeToSeconds(expiration, TimeUnit.HOURS.toSeconds(1));
				return inSeconds;
			}
			return expirationInSeconds;
		}
		
	}
	
	@Data
	public static class RedirectStrategyConfig {
		boolean contextRelative;
		boolean forceHttps;
		Integer httpsPort = 443;
		
		public boolean isForceHttps(){
			return forceHttps;
		}
	}
	
	@Data
	public static class MemoryUser {
		String password = "$2a$10$1Qrdb4WZcn7gDKrTfgJEAOZMOQRiUNWjuPcOmU520nLbrz2wHQlpa";//default is jfish
		String[] roles;
		String[] authorities;
		Long userId;
	}
	@Data
	public static class InterceptersConfig {
		String[] pathPatterns;
		String access;
	}

	@Data
	public static class CorsConfig {
		/**
		 * disable只是移除cors的配置类
		 */
		boolean disable;
		/***
		 * 允许所有预检请求
		 */
		boolean permitAllPreFlightRequest;
	}
	
	/***
	 * for StrictHttpFirewall
	 * @author way
	 *
	 */
	@Data
	public static class StrictHttpFirewallConfig {
		/***
		 * 是否允许url带有分号
		 */
		boolean allowSemicolon;
		/***
		 * 是否允许url带"//"
		 */
		boolean allowBackSlash;
	}
	
	@Data
	public static class SessionConfig {
		SessionCreationPolicy creationPolicy;
	}

}
