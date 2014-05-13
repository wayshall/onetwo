package org.onetwo.plugins.security.utils;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.encrypt.MDFactory;
import org.onetwo.plugins.security.SecurityPlugin;
import org.slf4j.Logger;

final public class SecurityPluginUtils {
	
	private static Logger logger = MyLoggerFactory.getLogger(SecurityPluginUtils.class);

	public static final String LOGIN_PARAM_CLIENT_CODE = "clientCode";
	public static final String DEFAULT_SSO_SIGN_KEY = "podsf^&pk&$@[ko@#$s0df]pips9";
	public static final String SSO_USERSERVICE_EXPORTER_NAME = "ssoUserServiceExporter";
	
	public static String getSsoUserServiceExporterDefaultUrl(String baseUrl){
		return baseUrl + SecurityPlugin.getInstance().getPluginMeta().getPluginInfo().getContextPath() + "/" + SSO_USERSERVICE_EXPORTER_NAME;
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
	
	private SecurityPluginUtils(){
	}
}
