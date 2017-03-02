package org.onetwo.boot.module.security;

import javax.servlet.ServletContext;

import lombok.ToString;

import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.filter.WebContextConfigProvider;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;


/***
 * @author way
 *
 */
@ConfigurationProperties(prefix="jfish.security")
@ToString
public class BootSecurityConfig extends SecurityConfig implements WebContextConfigProvider {
	
	@Autowired
	private BootSiteConfig bootSiteConfig;
	
	public Boolean getSyncPermissionData(){
		if(this.syncPermissionData==null){
			return !bootSiteConfig.isProduct();
		}
		return this.syncPermissionData;
	}
	
	public String getUserLogoutUrl(){
		String url = this.getLogoutUrl();
		if(isCasEnabled()){
			url = this.getCas().getLogoutUrl();
		}
		return bootSiteConfig.getBaseURL() + StringUtils.appendStartWithSlash(url);
	}
	
	public String getAfterLoginUrl(){
		String url = super.getAfterLoginUrl();
		if(!url.startsWith(bootSiteConfig.getBaseURL())){
			url = bootSiteConfig.getBaseURL() + url;
		}
		return url;
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
