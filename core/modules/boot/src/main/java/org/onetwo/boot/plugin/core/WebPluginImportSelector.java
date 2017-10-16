package org.onetwo.boot.plugin.core;

import org.onetwo.boot.core.condition.EnabledKeyCondition;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author wayshall
 * <br/>
 */
public class WebPluginImportSelector extends EnabledKeyCondition
									implements EnvironmentAware, ImportBeanDefinitionRegistrar{

	private static final String PLUGIN_KEY = "jfish.plugin.";
	
	private WebPlugin webPlugin;
	private Environment environment;

	@Override
	protected String getEnabledKey(Environment environment, AnnotationAttributes attrubutes) {
		PluginMeta meta = parsePlugin(attrubutes).getPluginMeta();
		String key = PLUGIN_KEY+meta.getName()+".enabled";
		return key;
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
		AnnotationAttributes attrubutes = getAnnotationAttributes(metadata);
		String key = getEnabledKey(getEnvironment(), attrubutes);
		if(!isEnabled(getEnvironment(), key)){
			return ;
		}
		SpringUtils.registerBeanDefinition(registry, this.webPlugin.getClass().getName(), this.webPlugin.getClass());
	}

	private WebPlugin parsePlugin(AnnotationAttributes attributes){
		WebPlugin webPlugin = this.webPlugin;
		if(webPlugin==null){
			Class<? extends WebPlugin> pluginClass = attributes.getClass("webPluginClass");
			if(pluginClass==WebPlugin.class){
				throw new BaseException("you must be set your plugin class to annotation @"+JFishWebPlugin.class.getSimpleName());
			}
			webPlugin = ReflectUtils.newInstance(pluginClass);
			this.webPlugin = webPlugin;
		}
		return webPlugin;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}
