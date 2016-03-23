package org.onetwo.common.web.filter;

import javax.servlet.ServletContext;

public interface SiteConfigProvider<T extends SiteConfig> {

	T initWebConfig(ServletContext servletContext);
//	Object createWebConfig(FilterConfig config);
	
}
