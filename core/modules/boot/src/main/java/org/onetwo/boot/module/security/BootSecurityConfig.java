package org.onetwo.boot.module.security;

import javax.servlet.ServletContext;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.filter.WebContextConfigProvider;
import org.onetwo.ext.security.utils.ExceptionUserCheckerConfig;
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
	public static final String SECURITY_PREFIX = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".security";
	public static final String EXCEPTION_USER_CHECKER_ENABLE_KEY = SECURITY_PREFIX+".exceptionUserChecker";
	
	public static final String METADATA_SOURCE_KEY = "metadataSource";
	public static final String METADATA_SOURCE_DATABASE = "database";
	public static final String METADATA_SOURCE_NONE = "none";
	
	@Autowired(required=false)
	private BootSiteConfig bootSiteConfig;
	@Autowired(required=false)
	private BootJFishConfig bootJFishConfig;
	private String baseURL;
	
	ExceptionUserCheckerConfig exceptionUserChecker = new ExceptionUserCheckerConfig();
	
	/*public Boolean getSyncPermissionData(){
		if(this.syncPermissionData==null){
			return bootSiteConfig!=null && !bootSiteConfig.isProduct();
		}
		return this.syncPermissionData;
	}*/
	
	/***
	 * 是否记录相关的security日志
	 */
	public boolean isLogSecurityError() {
		if (bootJFishConfig!=null && bootJFishConfig.isAlwaysLogErrorDetail()) {
			return true;
		}
		return super.isLogSecurityError();
	}
	
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

}
