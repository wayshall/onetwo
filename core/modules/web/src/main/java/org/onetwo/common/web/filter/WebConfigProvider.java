package org.onetwo.common.web.filter;

import org.onetwo.common.web.config.BaseSiteConfig;

public interface WebConfigProvider {

	Object createWebConfig(BaseSiteConfig siteConfig);
	
}
