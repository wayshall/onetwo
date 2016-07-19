package org.onetwo.boot.plugins.security;

import javax.servlet.FilterConfig;

import lombok.ToString;

import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.common.spring.SpringApplication;
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


}
