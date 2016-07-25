package org.onetwo.common.web.filter;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;

public interface WebContextConfigProvider {

//	AppConfig createAppConfig(FilterConfig config);
	default String getConfigName(){
		return StringUtils.uncapitalize(getClass().getSimpleName());
	}
	default Object getWebConfig(ServletContext config){
		return this;
	}
	
}
