package org.onetwo.common.spring.ftl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirFreemarkerConfig {

	@Bean
	public DirsFreemarkerTemplateConfigurer dirFreemarkerTemplateConfigurer(){
		DirsFreemarkerTemplateConfigurer dirconfig = new DirsFreemarkerTemplateConfigurer();
		dirconfig.setTemplatePaths("/template/");
		dirconfig.initialize();
		return dirconfig;
	}
	
	@Bean
	public TemplateParser dirTemplateParser(){
		DefaultTemplateParser parser = new DefaultTemplateParser(dirFreemarkerTemplateConfigurer());
		return parser;
	}
}
