package org.onetwo.common.spring.web.mvc.config;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.context.SpringProfilesWebApplicationContext;
import org.slf4j.Logger;

/******
 * mvc上下问初始化
 * initialize in dispatcher servlet
 * @author wayshall
 *
 */
public class JFishMvcApplicationContext extends SpringProfilesWebApplicationContext {
	
	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	public JFishMvcApplicationContext(){
		this.setPluginManagerInitializer(new JFishPluginManagerInitializer());
	}
	
}
