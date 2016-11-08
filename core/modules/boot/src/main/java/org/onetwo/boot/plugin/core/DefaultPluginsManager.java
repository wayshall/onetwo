package org.onetwo.boot.plugin.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.onetwo.boot.plugin.ftl.PluginNameParser;
import org.onetwo.boot.plugin.mvc.annotation.WebPluginController;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

import com.google.common.collect.Maps;


public class DefaultPluginsManager implements InitializingBean, PluginManager {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ApplicationContext applicationContext;

	private final Map<String, WebPlugin> pluginMapping = Maps.newHashMap();
	private final Map<Class<? extends WebPlugin>, WebPlugin> pluginClassMapping = Maps.newHashMap();
	private final PluginNameParser pluginNameParser = new PluginNameParser();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		List<WebPlugin> plugins = SpringUtils.getBeans(applicationContext, WebPlugin.class);
		logger.info("find plugins : {} ", plugins);
		plugins.stream().forEach(plugin->registerPlugin(plugin));
		logger.info("find plugins : {} ", pluginMapping);
	}
	
	public PluginNameParser getPluginNameParser() {
		return pluginNameParser;
	}

	public String getPluginTemplateBasePath(WebPlugin webPlugin) {
		return pluginNameParser.getPluginBasePath(webPlugin.getPluginMeta().getName());
	}

	@Override
	public String getPluginTemplateBasePath(String pluginName) {
		return getPluginTemplateBasePath(getPlugin(pluginName));
	}

	final public PluginManager registerPlugin(WebPlugin plugin){
		String pluginName = plugin.getPluginMeta().getName();
		if(pluginMapping.containsKey(pluginName)){
			throw new BaseException("plugin["+pluginName+"] has exists. find new plugin: "+plugin+", exists plugin:" + pluginMapping.get(pluginName));
		}
		pluginMapping.put(pluginName, plugin);
		pluginClassMapping.put(plugin.getClass(), plugin);
		if(logger.isDebugEnabled()){
			logger.debug("register plugin : " + pluginName);
		}
		return this;
	}
	
	@Override
	public Optional<WebPlugin> findPluginByElementClass(Class<?> elementClass){
		if(WebPlugin.class.isAssignableFrom(elementClass)){
			return Optional.ofNullable(pluginClassMapping.get(elementClass));
		}
		WebPluginController wpc = AnnotationUtils.findAnnotation(elementClass, WebPluginController.class);
		if(wpc!=null){
			Class<? extends WebPlugin> wpClass = wpc.value();
			return Optional.ofNullable(pluginClassMapping.get(wpClass));
		}
		return this.pluginMapping.values()
									.stream()
									.filter(p->p.contains(elementClass))
									.sorted((o1, o2)->o2.getRootClass().getPackage().getName().length()-o1.getRootClass().getPackage().getName().length())
									.findFirst();
	}

	@Override
	public Collection<WebPlugin> getPlugins() {
		return pluginMapping.values();
	}

	@Override
	public WebPlugin getPlugin(String pluginName) {
		return pluginMapping.get(pluginName);
	}
	
}
