package org.onetwo.plugins.security;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;
import org.onetwo.common.fish.plugin.JFishMvcConfigurerListenerAdapter;
import org.onetwo.common.spring.web.authentic.SpringTargetArgumentResolver;
import org.onetwo.common.spring.web.mvc.config.JFishMvcConfigurerListener;
import org.onetwo.common.spring.web.mvc.config.event.ArgumentResolverEvent;
import org.onetwo.plugins.security.client.SsoClientWebContext;
import org.onetwo.plugins.security.common.SecurityWebContext;
import org.onetwo.plugins.security.server.SsoServerWebContext;
import org.onetwo.plugins.security.utils.SecurityPluginUtils;


public class SecurityWebPlugin extends AbstractJFishPlugin<SecurityWebPlugin> {

	private static SecurityWebPlugin instance;
	
	
	public static SecurityWebPlugin getInstance() {
		return instance;
	}
	

	public static String getTemplatePath(String template) {
		return getInstance().getPluginMeta().getPluginConfig().getTemplatePath(template);
	}

	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(SecurityWebContext.class);
		if(SecurityPluginUtils.existServerConfig()){
			annoClasses.add(SsoServerWebContext.class);
		}else if(SecurityPluginUtils.existClientConfig()){
			annoClasses.add(SsoClientWebContext.class);
		}
	}

	@Override
	public void setPluginInstance(SecurityWebPlugin plugin){
		instance = plugin;
	}

	@Override
	public JFishMvcConfigurerListener getJFishMvcConfigurerListener() {
		return new JFishMvcConfigurerListenerAdapter(this){

			@Override
//			@Subscribe
			public void listening(ArgumentResolverEvent event) {
				event.registerArgumentResolver(new SpringTargetArgumentResolver());
			}
			
		};
	}


	@Override
	public boolean registerMvcResources() {
		return true;
	}

}
