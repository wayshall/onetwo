package org.onetwo.common.web.filter;

import jakarta.servlet.FilterConfig;

public interface WebFilterInitializers {

	public void onInit(FilterConfig config);

}