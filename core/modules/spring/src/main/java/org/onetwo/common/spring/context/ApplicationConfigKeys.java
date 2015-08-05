package org.onetwo.common.spring.context;

import org.onetwo.common.utils.propconf.AppConfig;

public final class ApplicationConfigKeys {
	
//	public static final String BASE_PACKAGE = AppConfig.JFISH_BASE_PACKAGES;//"jfish.base.packages";
	public static final String BASE_PACKAGE_EXPR = "${"+AppConfig.JFISH_BASE_PACKAGES+"}";
	
	private ApplicationConfigKeys(){
	}

}
