package org.onetwo.boot.plugin.core;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author wayshall
 * <br/>
 */
public class WebPluginImportSelector extends SpringBootCondition
									implements ImportBeanDefinitionRegistrar, EnvironmentAware, BeanClassLoaderAware {

	private static final String PLUGIN_KEY = "jfish.plugin.";
	
	private Environment environment;
	private ClassLoader beanClassLoader;
	private WebPlugin webPlugin;


	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
		PluginMeta meta = parsePlugin(getAnnotationAttributes(metadata)).getPluginMeta();
		if(!isPluginEnabled(context.getEnvironment(), meta)){
			return ConditionOutcome.noMatch("plugin ["+meta.getName()+"] is not enabled");
		}
		return ConditionOutcome.match();
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
		PluginMeta meta = parsePlugin(getAnnotationAttributes(metadata)).getPluginMeta();
		if(!isPluginEnabled(getEnvironment(), meta)){
			return ;
		}
		SpringUtils.registerBeanDefinition(registry, this.webPlugin.getClass().getName(), this.webPlugin.getClass());
	}

	protected boolean isPluginEnabled(Environment environment, PluginMeta meta){
		String key = PLUGIN_KEY+meta.getName()+".enabled";
		return new RelaxedPropertyResolver(environment).getProperty(key, Boolean.class, Boolean.TRUE);
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

	protected AnnotationAttributes getAnnotationAttributes(AnnotatedTypeMetadata metadata){
		//support @AliasFor
		AnnotationAttributes attributes = SpringUtils.getAnnotationAttributes(metadata, JFishWebPlugin.class);
		return attributes;
	}
	
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public ClassLoader getBeanClassLoader() {
		return beanClassLoader;
	}

	@Override
	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		this.beanClassLoader = beanClassLoader;
	}

}
