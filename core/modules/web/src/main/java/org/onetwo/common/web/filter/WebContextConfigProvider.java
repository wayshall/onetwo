package org.onetwo.common.web.filter;

import javax.servlet.FilterConfig;

public interface WebContextConfigProvider {

//	AppConfig createAppConfig(FilterConfig config);
	String getConfigName();
	Object getWebConfig(FilterConfig config);
	
}
