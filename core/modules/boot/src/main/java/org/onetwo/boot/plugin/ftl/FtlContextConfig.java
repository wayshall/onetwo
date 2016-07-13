package org.onetwo.boot.plugin.ftl;

import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.boot.ftl.ClassPathTldsLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Configuration
public class FtlContextConfig {
	public static final String WEBFTLS_PATH = "META-INF/resources/webftls";
	
	@Autowired
	protected FreeMarkerProperties properties;

	protected void applyProperties(FreeMarkerConfigurationFactory factory) {
		factory.setTemplateLoaderPaths(this.properties.getTemplateLoaderPath());
		factory.setPreferFileSystemAccess(this.properties.isPreferFileSystemAccess());
		factory.setDefaultEncoding(this.properties.getCharsetName());
		Properties settings = new Properties();
		settings.putAll(this.properties.getSettings());
		factory.setFreemarkerSettings(settings);
	}
	
	@Bean
	@ConditionalOnMissingBean(FreeMarkerConfig.class)
	public FreeMarkerConfigurer freeMarkerConfigurer() {
		PluginFreeMarkerConfigurer configurer = new PluginFreeMarkerConfigurer();
		applyProperties(configurer);
		String[] paths = this.properties.getTemplateLoaderPath();
		paths = ArrayUtils.add(paths, WEBFTLS_PATH);
		configurer.setTemplateLoaderPaths(paths);
		return configurer;
	}

	@Bean
	@ConditionalOnMissingBean(ClassPathTldsLoader.class)
	public ClassPathTldsLoader classPathTldsLoader(){
		return new ClassPathTldsLoader();
	}
}
