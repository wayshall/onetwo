package org.onetwo.common.fish.web;

import org.onetwo.common.fish.spring.config.JFishContextConfig;
import org.onetwo.common.spring.context.SpringProfilesWebApplicationContext;
import org.onetwo.common.web.config.BaseSiteConfig;

/*****
 * service上下文初始化
 * initialize in web app start 
 * config in web.xml
 * @author wayshall
 *
 */
public class JFishWebApplicationContext extends SpringProfilesWebApplicationContext {
	
	public JFishWebApplicationContext(String appEnvironment, Class<?>... outerContextClasses){
		setAppEnvironment(appEnvironment);
		setAnnotatedClasses(outerContextClasses);
	}
	
	public JFishWebApplicationContext(){
		this(BaseSiteConfig.getInstance().getAppEnvironment(), JFishContextConfig.class);
	}
	
}
