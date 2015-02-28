package org.onetwo.plugins.session.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.onetwo.common.web.config.BaseSiteConfig;

public class SpringSessionInitializer extends AbstractHttpSessionApplicationInitializer {
	
//	private static boolean initialized = false;
//	private SessionPluginConfig sessionConfig;
	
	public SpringSessionInitializer(){
		super(SessionPluginWebContext.class);
		/*JFishProperties properties = SpringUtils.loadAsJFishProperties(SessionPlugin.CONFIG_PATH);
		sessionConfig = new SessionPluginConfig();
		sessionConfig.load(properties);*/
	}


    public void onStartup(ServletContext servletContext) throws ServletException {
    	if(!BaseSiteConfig.getInstance().isContainerSession()){
        	super.onStartup(servletContext);
    	}
    }
}
