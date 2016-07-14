package org.onetwo.boot.plugin.core;

import java.util.List;
import java.util.Optional;

import org.onetwo.common.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.google.common.collect.ImmutableList;


public class DefaultPluginsManager implements InitializingBean, PluginManager {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private List<Plugin> plugins;
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		List<Plugin> plugins = SpringUtils.getBeans(applicationContext, Plugin.class);
		this.plugins = ImmutableList.copyOf(plugins);
		logger.info("find plugins : {} ", plugins);
		
	}

	/*public DefaultPluginsManager addPlugin(Plugin plugin){
		plugins.add(plugin);
		return this;
	}*/
	
	@Override
	public Optional<Plugin> findPluginByElementClass(Class<?> elementClass){
		return this.plugins.stream()
							.filter(p->p.contains(elementClass))
							.sorted((o1, o2)->o2.getRootClass().getPackage().getName().length()-o1.getRootClass().getPackage().getName().length())
							.findFirst();
	}

	@Override
	public List<Plugin> getPlugins() {
		return plugins;
	}
	
}
