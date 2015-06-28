package org.onetwo.common.web.config;

import org.onetwo.common.spring.context.ClassPathApplicationContext;
import org.onetwo.common.spring.context.SpringProfilesWebApplicationContext;

/*****
 * service上下文初始化
 * initialize in web app start 
 * config in web.xml
 * no plugin init, 
 * @see JFishWebApplicationContext
 * @author wayshall
 *
 */
public class WebProfilesApplicationContext extends SpringProfilesWebApplicationContext {
	
	public WebProfilesApplicationContext(String env){
		setAppEnvironment(env);
//		setAppEnvironment(BaseSiteConfig.getInstance().getAppEnvironment());
		setAnnotatedClasses(ClassPathApplicationContext.class);
	}
}
