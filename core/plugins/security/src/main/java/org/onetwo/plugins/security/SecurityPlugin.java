package org.onetwo.plugins.security;

import java.util.List;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.plugin.AbstractContextPlugin;
import org.onetwo.plugins.security.client.SsoClientContext;
import org.onetwo.plugins.security.common.SecurityContext;
import org.onetwo.plugins.security.server.SsoServerContext;
import org.springframework.core.io.Resource;


public class SecurityPlugin extends AbstractContextPlugin<SecurityPlugin> {

	private static SecurityPlugin instance;
	
	
	public static SecurityPlugin getInstance() {
		return instance;
	}
	
	@Override
	public void setPluginInstance(SecurityPlugin plugin){
		instance = plugin;
	}

	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(SecurityContext.class);
		Resource config = SpringUtils.classpath(SsoServerContext.SSO_SERVER_CONFIG_PATH);
		if(config.exists()){
			annoClasses.add(SsoServerContext.class);
		}
		config = SpringUtils.classpath(SsoClientContext.SSO_CLIENT_CONFIG_PATH);
		if(config.exists()){
			annoClasses.add(SsoClientContext.class);
		}
	}


}
