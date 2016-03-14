package appweb.web;

import javax.servlet.FilterConfig;

import org.onetwo.common.utils.propconf.AppConfig;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.filter.WebConfigProvider;
import org.springframework.stereotype.Component;

import appweb.utils.WebConfig;

@Component
public class WebConfigProviderImpl implements WebConfigProvider {

	@Override
	public Object createWebConfig(FilterConfig config) {
		return WebConfig.getInstance();
	}


	@Override
    public AppConfig createAppConfig(FilterConfig config) {
	    return BaseSiteConfig.getInstance();
    }
}
