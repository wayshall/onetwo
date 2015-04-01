package org.onetwo.plugins.melody.web;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import net.bull.javamelody.SessionListener;

import org.onetwo.common.fish.JFishUtils;
import org.onetwo.common.spring.plugin.ContextPluginUtils;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.plugins.melody.MelodyConfig;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;

/****
 * javamelody 自带了web-fragment.xml........
 * @author way
 *
 */
@Order(JFishUtils.WEBAPP_INITIALIZER_ORDER+10)
public class MelodyWebInitializer implements WebApplicationInitializer {
	
	public static final String FILTER_NAME = "javamelody";
	public static final String DISABLED_KEY = "javamelody.disabled";
	
	public MelodyWebInitializer(){
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		
		JFishProperties prop = ContextPluginUtils.loadPluginConfigs("melody", BaseSiteConfig.getInstance().getAppEnvironment());
		MelodyConfig config = new MelodyConfig();
		config.load(prop);
		
		if(config.isDisabled()){
			System.setProperty(DISABLED_KEY, "true");
			return ;
		}
		
		FilterRegistration fr = servletContext.getFilterRegistration(FILTER_NAME);
		if(fr==null){
			servletContext.addListener(SessionListener.class);
			fr = servletContext.addFilter(FILTER_NAME, AuthorityMonitoringFilter.class);
			fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.ASYNC), true, config.getUrlPatterns());
			
		}
		fr.setInitParameters(config.getMonitoringInitParams());
	}

}
