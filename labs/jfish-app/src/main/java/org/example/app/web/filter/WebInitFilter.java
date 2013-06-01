package org.example.app.web.filter;

import javax.servlet.FilterConfig;

import org.example.app.web.utils.WebConfig;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.filter.BaseInitFilter;

public class WebInitFilter extends BaseInitFilter {

	@Override
	protected Object getWebConfig(BaseSiteConfig siteConfig) {
		return WebConfig.getInstance();
	}

	@Override
	protected void initApplication(FilterConfig config) {
		super.initApplication(config);
	}

}
