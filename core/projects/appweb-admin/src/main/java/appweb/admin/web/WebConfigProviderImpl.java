package appweb.admin.web;

import javax.servlet.FilterConfig;

import org.onetwo.common.utils.propconf.AppConfig;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.filter.AppConfigProvider;
import org.onetwo.common.web.filter.WebContextConfigProvider;
import org.springframework.stereotype.Component;

import appweb.admin.utils.WebConfig;

@Component
public class WebConfigProviderImpl implements AppConfigProvider, WebContextConfigProvider {
	
	

	@Override
    public String getConfigName() {
	    return "webConfig";
    }

	@Override
    public Object getWebConfig(FilterConfig config) {
	    return WebConfig.getInstance();
    }

	@Override
    public AppConfig createAppConfig(FilterConfig config) {
	    return BaseSiteConfig.getInstance();
    }

}
