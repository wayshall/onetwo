package org.onetwo.common.spring.web.mvc.config;

import org.onetwo.common.spring.context.SpringProfilesWebApplicationContext;

/******
 * mvc上下问初始化
 * initialize in dispatcher servlet
 * @author wayshall
 *	
 * call onMvcContextClasses
 */
public class JFishMvcApplicationContext extends SpringProfilesWebApplicationContext {
	
	public JFishMvcApplicationContext(){
		this.setPluginManagerInitializer(new JFishWebMvcPluginManagerInitializer());
	}
	
}
