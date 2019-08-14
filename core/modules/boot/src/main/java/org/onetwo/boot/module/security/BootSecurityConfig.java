package org.onetwo.boot.module.security;

import javax.servlet.ServletContext;

import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.filter.WebContextConfigProvider;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;


/***
 * @author way
 *
 */
@ConfigurationProperties(prefix=BootSecurityConfig.SECURITY_PREFIX)
@Data
@EqualsAndHashCode(callSuper=false)
public class BootSecurityConfig extends SecurityConfig implements WebContextConfigProvider {
	public static final String SECURITY_PREFIX = "jfish.security";
	public static final String EXCEPTION_USER_CHECKER_ENABLE_KEY = SECURITY_PREFIX+".exceptionUserChecker";
	
	public static final String METADATA_SOURCE_KEY = "metadataSource";
	public static final String METADATA_SOURCE_DATABASE = "database";
	public static final String METADATA_SOURCE_NONE = "none";
	
	@Autowired(required=false)
	private BootSiteConfig bootSiteConfig;
	private String baseURL;
	
	ExceptionUserCheckerConfig exceptionUserChecker = new ExceptionUserCheckerConfig();
	
	/*public Boolean getSyncPermissionData(){
		if(this.syncPermissionData==null){
			return bootSiteConfig!=null && !bootSiteConfig.isProduct();
		}
		return this.syncPermissionData;
	}*/
	
	@Override
	public String getUserLogoutUrl(){
		String url = this.getLogoutUrl();
		if(isCasEnabled()){
			url = this.getCas().getLogoutUrl();
		}
		return getBaseURL() + StringUtils.appendStartWithSlash(url);
	}
	
	public String getAfterLoginUrl(){
		String url = super.getAfterLoginUrl();
		String baseUrl = getBaseURL();
		if(StringUtils.isNotBlank(baseUrl) && !url.startsWith(baseUrl)){
			url = baseUrl + url;
		}
		String ctxPath = bootSiteConfig.getContextPath();
		if(StringUtils.isNotBlank(ctxPath) && url.startsWith(ctxPath)){
			url = url.substring(ctxPath.length());
		}
		return url;
	}

	public String getBaseURL() {
		if(baseURL!=null){
			return baseURL;
		}else if(bootSiteConfig!=null){
			return bootSiteConfig.getBaseURL();
		}
		return null;
	}
	
	public boolean isCasEnabled(){
		return Springs.getInstance().containsClassBean("org.springframework.security.cas.web.CasAuthenticationFilter");
	}
	

	@Override
	public String getConfigName() {
		return "securityConfig";
	}

	@Override
	public Object getWebConfig(ServletContext servletContext) {
		return this;
	}

	@Data
	public static class ExceptionUserCheckerConfig {
		private String duration = "1m";
		private int maxLoginTimes = 5;
	}

}
