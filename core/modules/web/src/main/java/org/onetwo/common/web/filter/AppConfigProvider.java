package org.onetwo.common.web.filter;

import javax.servlet.FilterConfig;

import org.onetwo.common.utils.propconf.AppConfig;

public interface AppConfigProvider {

	AppConfig createAppConfig(FilterConfig config);
//	Object createWebConfig(FilterConfig config);
	
}
