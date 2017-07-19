package org.onetwo.boot.plugin.ftl;

import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.ftl.ClassPathTldsLoader;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.SpringUtils.WithAnnotationBeanData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Configuration
public class WebFtlsContextConfig {
//	public static final String WEBFTLS_PATH = "classpath*:META-INF/resources/webftls";
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	protected FreeMarkerProperties properties;
	@Autowired
	protected ApplicationContext applicationContext;

	protected void applyProperties(FreeMarkerConfigurationFactory factory) {
		factory.setTemplateLoaderPaths(this.properties.getTemplateLoaderPath());
		factory.setPreferFileSystemAccess(this.properties.isPreferFileSystemAccess());
		factory.setDefaultEncoding(this.properties.getCharsetName());
		Properties settings = new Properties();
		settings.putAll(this.properties.getSettings());
		factory.setFreemarkerSettings(settings);
	}
	
	@Bean
	@ConditionalOnMissingBean({FreeMarkerConfig.class, FreeMarkerViewResolver.class})
	public FreeMarkerConfigurer freeMarkerConfigurer() {
		PluginFreeMarkerConfigurer configurer = new PluginFreeMarkerConfigurer();
		applyProperties(configurer);
		String[] paths = this.properties.getTemplateLoaderPath();
//		paths = ArrayUtils.add(paths, WEBFTLS_PATH);
		configurer.setTemplateLoaderPaths(paths);
		
		List<WithAnnotationBeanData<FreeMarkerViewTools>> tools = SpringUtils.getBeansWithAnnotation(applicationContext, FreeMarkerViewTools.class);
		tools.forEach(t->{
			String name = t.getAnnotation().value();
			if(StringUtils.isBlank(name)){
				name = t.getBean().getClass().getSimpleName();
			}
			configurer.setFreemarkerVariable(name, t.getBean());
			logger.info("registered FreeMarkerViewTools : {}", name);
		});
		return configurer;
	}

	@Bean
	@ConditionalOnMissingBean(ClassPathTldsLoader.class)
	public ClassPathTldsLoader classPathTldsLoader(){
		return new ClassPathTldsLoader();
	}
	
	@Bean
	public PluginHelperViewTools pluginViewTools(){
		return new PluginHelperViewTools();
	}
}
