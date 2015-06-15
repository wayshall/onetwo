package org.onetwo.common.fish.web;

import org.onetwo.common.fish.spring.config.JFishContextConfig;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.context.SpringProfilesWebApplicationContext;
import org.onetwo.common.spring.web.mvc.config.JFishPluginManagerInitializer;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.AppConfig;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.context.support.StandardServletEnvironment;

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
		
		String basePackage = BaseSiteConfig.getInstance().getJFishBasePackages();
		if(StringUtils.isBlank(basePackage)){
			basePackage = SpringUtils.loadAsJFishProperties("webconf/application.properties").getProperty(AppConfig.JFISH_BASE_PACKAGES);
		}
		if(StringUtils.isNotBlank(basePackage)){
			System.setProperty(AppConfig.JFISH_BASE_PACKAGES, basePackage);
		}
	}
	
	public JFishWebApplicationContext(){
		this(BaseSiteConfig.getInstance().getAppEnvironment(), JFishContextConfig.class);
	}

	
	protected ConfigurableEnvironment createEnvironment() {
		return new StandardServletEnvironment();
	}
}
