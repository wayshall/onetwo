package org.onetwo.common.fish.web;

import org.onetwo.common.fish.plugin.JFishPluginManagerFactory;
import org.onetwo.common.fish.spring.config.JFishContextConfig;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.context.AbstractJFishAnnotationConfig;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.common.web.config.BaseSiteConfig;

/*****
 * service上下文初始化
 * initialize in web app start 
 * config in web.xml
 * @author wayshall
 *
 */
public class JFishWebApplicationContext extends AbstractJFishAnnotationConfig {
	

	public JFishWebApplicationContext(){
		this(null);
	}
	
	public JFishWebApplicationContext(Class<?>[] outerContextClasses){
		SpringUtils.setProfiles(BaseSiteConfig.getInstance().getAppEnvironment());
		JFishPluginManagerFactory.initPluginManager();
		
		final JFishList<Class<?>> contextClasses = JFishList.create();
		contextClasses.add(JFishContextConfig.class);
		contextClasses.addArray(outerContextClasses);

		JFishPluginManagerFactory.getPluginManager().registerPluginJFishContextClasses(contextClasses);
		
		this.register(contextClasses.toArray(new Class[contextClasses.size()]));
	}
	
}
