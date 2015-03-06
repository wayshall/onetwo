package org.onetwo.common.fish.web;

import org.onetwo.common.fish.spring.config.JFishContextConfig;
import org.onetwo.common.spring.context.SpringProfilesWebApplicationContext;
import org.onetwo.common.spring.web.mvc.config.JFishPluginManagerInitializer;
import org.onetwo.common.web.config.BaseSiteConfig;

/*****
 * webapp(jfish)上下文初始化
 * initialize in web app start 
 * config in web.xml
 * @author wayshall
 *
 * call onJFishContextClasses
 */
public class JFishWebApplicationContext extends SpringProfilesWebApplicationContext {
	
	public JFishWebApplicationContext(String appEnvironment, Class<?>... outerContextClasses){
		setAppEnvironment(appEnvironment);
		setAnnotatedClasses(outerContextClasses);
		this.setPluginManagerInitializer(new JFishPluginManagerInitializer());
	}
	
	public JFishWebApplicationContext(){
		this(BaseSiteConfig.getInstance().getAppEnvironment(), JFishContextConfig.class);
	}
	
}
