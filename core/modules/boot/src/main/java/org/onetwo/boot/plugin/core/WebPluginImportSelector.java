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
									implements EnvironmentAware, ImportBeanDefinitionRegistrar/*, DeferredImportSelector*/ {

	private static final String PLUGIN_KEY = "jfish.plugin.";
	
	private WebPlugin webPlugin;
	private Environment environment;
	
	/*private Class<JFishWebPlugin> annotationClass = JFishWebPlugin.class;

	public Class<?> getAnnotationClass() {
		return annotationClass;
	}


	@Override
	public String[] selectImports(AnnotationMetadata metadata) {
		AnnotationAttributes attrubutes = getAnnotationAttributes(metadata);
		String key = getEnabledKey(environment, attrubutes);
		if(!isEnabled(environment, key)){
			return new String[0];
		}

		AnnotationAttributes attributes = AnnotationAttributes.fromMap(
				metadata.getAnnotationAttributes(this.getAnnotationClass().getName(), true));

		Assert.notNull(attributes, "No " + getAnnotationClass().getSimpleName() + " attributes found. Is "
				+ metadata.getClassName() + " annotated with @" + getAnnotationClass().getSimpleName() + "?");
		
		List<String> factories = new ArrayList<>(new LinkedHashSet<>(SpringFactoriesLoader.loadFactoryNames(this.getAnnotationClass(), this.getBeanClassLoader())));
		if(log.isInfoEnabled()){
			log.info("found {} jfish plugin configuration: {}", factories.size(), factories);
		}
		return factories.toArray(new String[factories.size()]);
	}*/

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
