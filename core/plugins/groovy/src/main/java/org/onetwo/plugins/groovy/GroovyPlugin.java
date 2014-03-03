package org.onetwo.plugins.groovy;

import java.util.List;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.plugin.AbstractContextPlugin;
import org.onetwo.plugins.groovy.model.PluginModelContext;
import org.springframework.core.io.Resource;



public class GroovyPlugin extends AbstractContextPlugin<GroovyPlugin> {

	private static GroovyPlugin instance;
	
	
	public static GroovyPlugin getInstance() {
		return instance;
	}

	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		Resource res = SpringUtils.classpath(PluginModelContext.GROOVY_CONFIG_PATH);
		if(res.exists()){
			annoClasses.add(PluginModelContext.class);
		}else{
			logger.info("no groovy config found, ignore...");
		}
	}

	public void setPluginInstance(GroovyPlugin plugin){
		instance = plugin;
	}

}
