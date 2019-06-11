package org.onetwo.common.spring.ftl;

import java.util.Map;

import org.onetwo.common.spring.ftl.StringFreemarkerTemplateConfigurer.StringTemplate;

/**
 * @author weishao zeng
 * <br/>
 */
public abstract class Ftls {
	
	static public TemplateParser createParser(Map<String, ? extends StringTemplate> templates) {
		StringFreemarkerTemplateConfigurer freemarkerTemplateConfigurer = new StringFreemarkerTemplateConfigurer();
		templates.forEach((name, template) -> {
			freemarkerTemplateConfigurer.putTemplate(name, template);
		});
		freemarkerTemplateConfigurer.initialize();
		TemplateParser parser = new DefaultTemplateParser(freemarkerTemplateConfigurer);
		return parser;
	}
	
	static public TemplateParser createStringParser(Map<String, String> templates) {
		StringFreemarkerTemplateConfigurer freemarkerTemplateConfigurer = new StringFreemarkerTemplateConfigurer();
		templates.forEach((name, template) -> {
			freemarkerTemplateConfigurer.putTemplate(name, template);
		});
		freemarkerTemplateConfigurer.initialize();
		TemplateParser parser = new DefaultTemplateParser(freemarkerTemplateConfigurer);
		return parser;
	}

}
