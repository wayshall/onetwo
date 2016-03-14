package org.onetwo.common.web.filter;

import javax.servlet.FilterConfig;

public interface SiteConfigProvider<T extends SiteConfig> {

	T createConfig(FilterConfig config);
//	Object createWebConfig(FilterConfig config);
	
}
