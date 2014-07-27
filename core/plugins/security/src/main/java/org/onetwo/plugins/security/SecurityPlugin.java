package org.onetwo.plugins.security;

import java.util.List;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;
import org.onetwo.plugins.security.client.SsoClientContext;
import org.onetwo.plugins.security.common.SecurityContext;
import org.onetwo.plugins.security.server.SsoServerContext;
import org.onetwo.plugins.security.utils.SecurityPluginUtils;


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
		
		if(SecurityPluginUtils.existServerConfig()){
			annoClasses.add(SsoServerContext.class);
			
		}else if(SecurityPluginUtils.existClientConfig()){
			annoClasses.add(SsoClientContext.class);
		}
	}


}
