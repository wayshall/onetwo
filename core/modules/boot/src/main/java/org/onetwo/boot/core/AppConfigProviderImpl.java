package org.onetwo.boot.core;

import javax.servlet.FilterConfig;

import org.onetwo.common.propconf.AppConfig;
import org.onetwo.common.web.filter.AppConfigProvider;
import org.springframework.stereotype.Component;

@Component
public class AppConfigProviderImpl implements AppConfigProvider {

	@Override
    public AppConfig createAppConfig(FilterConfig config) {
	    // TODO Auto-generated method stub
	    return null;
    }


}
