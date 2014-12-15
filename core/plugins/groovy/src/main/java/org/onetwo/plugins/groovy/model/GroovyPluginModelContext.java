package org.onetwo.plugins.groovy.model;

import org.onetwo.plugins.groovy.GroovyBeanFactory;
import org.onetwo.plugins.groovy.GroovyPlugin;
import org.onetwo.plugins.groovy.GroovyPluginConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scripting.support.ScriptFactoryPostProcessor;


@Configuration
//@ComponentScan(basePackageClasses=PluginModelContext.class)
public class GroovyPluginModelContext {

//	private static final String GROOVY_CONFIG_BASE = "/groovy/groovy-config";
//	public static final String GROOVY_CONFIG_PATH = GROOVY_CONFIG_BASE + ".properties";
	
	@Bean
	public GroovyPluginConfig groovyPluginConfig(){
		return GroovyPlugin.getInstance().getConfig();
	}
	
	/*@Bean
	public PropertiesFactoryBean groovyPluginProperties() {
		String envLocation = GROOVY_CONFIG_BASE + "-" + appConfig.getAppEnvironment() + ".properties";
		return SpringUtils.createPropertiesBySptring(GROOVY_CONFIG_PATH, envLocation);
	}*/

	@Bean
	public ScriptFactoryPostProcessor scriptFactoryPostProcessor(){
		return new ScriptFactoryPostProcessor();
	}
	
	@Bean
	public GroovyBeanFactory groovyBeanFactory(){
		GroovyBeanFactory gbf =  new GroovyBeanFactory();
		GroovyPluginConfig config = groovyPluginConfig();
		gbf.setPackagesToScan(config.getGroovySourcePackages().toArray(new String[0]));
		return gbf;
	}
}
