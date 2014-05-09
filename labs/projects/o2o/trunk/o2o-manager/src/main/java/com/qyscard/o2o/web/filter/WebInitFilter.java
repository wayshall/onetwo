package com.qyscard.o2o.web.filter;

import javax.servlet.FilterConfig;

import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.filter.BaseInitFilter;

import com.qyscard.o2o.WebConfig;



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
