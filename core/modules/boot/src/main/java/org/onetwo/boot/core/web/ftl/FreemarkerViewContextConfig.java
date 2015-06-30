package org.onetwo.boot.core.web.ftl;

import java.util.HashMap;
import java.util.Map;

import org.onetwo.common.web.view.ftl.DefineDirective;
import org.onetwo.common.web.view.ftl.ExtendsDirective;
import org.onetwo.common.web.view.ftl.OverrideDirective;
import org.onetwo.common.web.view.ftl.ProfileDirective;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import freemarker.template.SimpleHash;

@Configuration
public class FreemarkerViewContextConfig implements InitializingBean {

	/*@Autowired
	protected FreeMarkerProperties properties;*/
	
	@Autowired
	private freemarker.template.Configuration freeMarkerConfiguration;
	/*
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;*/
	
	public FreemarkerViewContextConfig(){
	}
	
	@Bean
	@ConditionalOnMissingBean(ClassPathTldsLoader.class)
	public ClassPathTldsLoader classPathTldsLoader(){
		return new ClassPathTldsLoader();
	}

	@Override
    public void afterPropertiesSet() throws Exception {
		Map<String, Object> freemarkerVariables = new HashMap<String, Object>();

		freemarkerVariables.put(ExtendsDirective.DIRECTIVE_NAME, new ExtendsDirective());
		freemarkerVariables.put(DefineDirective.DIRECTIVE_NAME, new DefineDirective());
		freemarkerVariables.put(OverrideDirective.DIRECTIVE_NAME, new OverrideDirective());
		freemarkerVariables.put(ProfileDirective.DIRECTIVE_NAME, new ProfileDirective());
		
//		freeMarkerConfiguration.setObjectWrapper(INSTANCE);
		freeMarkerConfiguration.setSetting("classic_compatible", "true");
		//默认不格式化数字
		freeMarkerConfiguration.setNumberFormat("#");
		
		freeMarkerConfiguration.setAllSharedVariables(new SimpleHash(freemarkerVariables, freeMarkerConfiguration.getObjectWrapper()));
    }


	/*protected void applyProperties(FreeMarkerConfigurationFactory factory) {
		factory.setTemplateLoaderPaths(this.properties.getTemplateLoaderPath());
		factory.setDefaultEncoding(this.properties.getCharset());
		Properties settings = new Properties();
		settings.putAll(this.properties.getSettings());
		factory.setFreemarkerSettings(settings);
	}
	
	@Bean
	public JFishFreeMarkerConfigurer jfishFreeMarkerConfigurer(){
		JFishFreeMarkerConfigurer config = new JFishFreeMarkerConfigurer();
		applyProperties(config);
		return config;
	}*/
	
	/*@Bean
	public SpringBootConfig springBootConfig(){
		SpringBootConfig config = new SpringBootConfig();
		return config;
	}*/

}
