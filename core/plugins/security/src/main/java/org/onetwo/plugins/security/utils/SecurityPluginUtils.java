package org.onetwo.plugins.security.utils;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.encrypt.MDFactory;
import org.onetwo.plugins.security.SecurityPlugin;
import org.onetwo.plugins.security.client.SsoClientContext;
import org.onetwo.plugins.security.server.SsoServerContext;
import org.onetwo.plugins.session.web.JFishCookiesHttpSessionStrategy;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.util.Assert;

final public class SecurityPluginUtils {
	
	private static Logger logger = MyLoggerFactory.getLogger(SecurityPluginUtils.class);

	public static final String LOGIN_PARAM_CLIENT_CODE = "clientCode";
	public static final String LOGIN_PARAM_ALL = "all";
	public static final String DEFAULT_SSO_SIGN_KEY = "podsf^&pk&$@[ko@#$s0df]pips9";
	public static final String SSO_USERSERVICE_EXPORTER_NAME = "ssoUserServiceExporter";
	
	public static String getSsoUserServiceExporterDefaultUrl(String baseUrl){
		return baseUrl + SecurityPlugin.getInstance().getPluginMeta().getPluginInfo().getContextPath() + "/" + SSO_USERSERVICE_EXPORTER_NAME;
	}
	
	public static String getBasedPluginUrl(String baseUrl){
		return baseUrl + SecurityPlugin.getInstance().getPluginMeta().getPluginInfo().getContextPath();
	}
	
	public static String getClientLoginUrl(String baseUrl){
		return baseUrl + SecurityPlugin.getInstance().getPluginMeta().getPluginInfo().getContextPath() + "/client/ssologin";
	}
	
	public static String getClientLogoutUrl(String baseUrl){
		return baseUrl + SecurityPlugin.getInstance().getPluginMeta().getPluginInfo().getContextPath() + "/client/ssologout";
	}

	
	public static boolean checkSign(String token, String signKey, String sign){
		String source = LangUtils.appendNotBlank(token, "|", signKey);
		boolean valid = MDFactory.createSHA().checkEncrypt(source, sign);
		logger.error("check sign, source[{}] : {}", source, sign);
		return valid;
	}

	
	public static String sign(String token, String signKey){
		String source = LangUtils.appendNotBlank(token, "|", signKey);
		String entry = MDFactory.createSHA().encryptWithSalt(source);
		logger.error("sign token[{}] : {}", source, entry);
		return entry;
	}
	
	public static boolean existServerConfig(){
		Resource config = SpringUtils.classpath(SsoServerContext.SSO_SERVER_CONFIG_PATH);
		return config.exists();
	}
	
	public static boolean existClientConfig(){
		Resource config = SpringUtils.classpath(SsoClientContext.SSO_CLIENT_CONFIG_PATH);
		return config.exists();
	}
	
	public static HttpSessionStrategy getHttpSessionStrategy(){
		HttpSessionStrategy hss = SpringApplication.getInstance().getBean(HttpSessionStrategy.class);
		Assert.notNull(hss, "no HttpSessionStrategy found!");
		return hss;
	}
	
	public static JFishCookiesHttpSessionStrategy getJFishCookiesHttpSessionStrategy(){
		JFishCookiesHttpSessionStrategy hss = SpringApplication.getInstance().getBean(JFishCookiesHttpSessionStrategy.class);
		Assert.notNull(hss, "no HttpSessionStrategy found!");
		return hss;
	}
	
	private SecurityPluginUtils(){
	}
}
