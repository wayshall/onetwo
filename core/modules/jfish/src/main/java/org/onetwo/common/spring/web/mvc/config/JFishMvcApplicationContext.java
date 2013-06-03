package org.onetwo.common.spring.web.mvc.config;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.fish.plugin.JFishPluginManagerFactory;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.context.AbstractJFishAnnotationConfig;
import org.slf4j.Logger;

/******
 * mvc上下问初始化
 * @author wayshall
 *
 */
public class JFishMvcApplicationContext extends AbstractJFishAnnotationConfig {
	
	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	public JFishMvcApplicationContext(){
		final List<Class<?>> annoClasses = new ArrayList<Class<?>>();
		annoClasses.add(JFishMvcConfig.class);
		
		JFishPluginManagerFactory.getPluginManager().registerPluginMvcContextClasses(annoClasses);
		
		this.register(annoClasses.toArray(new Class[annoClasses.size()]));
	}
}
