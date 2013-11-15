package org.onetwo.common.web.config;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.context.AbstractJFishAnnotationConfig;
import org.onetwo.common.utils.list.JFishList;

/*****
 * service上下文初始化
 * initialize in web app start 
 * config in web.xml
 * no plugin init, 
 * @see JFishWebApplicationContext
 * @author wayshall
 *
 */
public class WebProfilesApplicationContext extends AbstractJFishAnnotationConfig {
	

	public WebProfilesApplicationContext(){
		this(null);
	}
	
	public WebProfilesApplicationContext(Class<?>[] outerContextClasses){
		SpringUtils.setProfiles(BaseSiteConfig.getInstance().getAppEnvironment());
		
		final JFishList<Class<?>> contextClasses = JFishList.create();
		contextClasses.add(ClassPathApplicationContext.class);
		contextClasses.addArray(outerContextClasses);

		this.register(contextClasses.toArray(new Class[contextClasses.size()]));
	}
	
}
