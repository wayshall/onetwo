package org.onetwo.common.fish.web;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.fish.plugin.JFishPluginManagerFactory;
import org.onetwo.common.fish.spring.config.JFishContextConfig;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.context.AbstractJFishAnnotationConfig;
import org.onetwo.common.web.config.BaseSiteConfig;

public class JFishWebApplicationContext extends AbstractJFishAnnotationConfig {
	
	public JFishWebApplicationContext(){
		SpringUtils.setProfiles(BaseSiteConfig.getInstance().getAppEnvironment());
		JFishPluginManagerFactory.initPluginManager();
		
		final List<Class<?>> contextClasses = new ArrayList<Class<?>>();
		contextClasses.add(JFishContextConfig.class);

		JFishPluginManagerFactory.getPluginManager().registerPluginJFishContextClasses(contextClasses);
		
		this.register(contextClasses.toArray(new Class[contextClasses.size()]));
	}
	
}
