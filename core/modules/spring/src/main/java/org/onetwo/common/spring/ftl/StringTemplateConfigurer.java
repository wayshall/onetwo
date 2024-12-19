package org.onetwo.common.spring.ftl;

import java.io.StringWriter;

import org.onetwo.common.exception.BaseException;

import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class StringTemplateConfigurer extends AbstractFreemarkerTemplateConfigurer implements TemplateParser {
	
	public StringTemplateConfigurer() {
	}

	@Override
	protected TemplateLoader getTempateLoader() {
		return new StringTemplateLoader();
	}
	
	@Override
	protected void buildConfigration(Configuration cfg) {
		// 方括号
		cfg.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
	}

	@Override
	public String parse(String content, Object context) {
		Template template = getTemplate(content);
		StringWriter stringWriter = new StringWriter();
		try {
			template.process(context, stringWriter);
		} catch (Exception e) {
			throw new BaseException("parse tempalte["+content+"] error : " + e.getMessage(), e);
		}
		return stringWriter.toString();
	}

	public Template getTemplate(String templateContent){
		Template template;
		try {
//			template = new Template(LangUtils.randomUUID(), templateContent, getConfiguration());
			Configuration config = getConfiguration();
			template = new Template(templateContent, templateContent, config);
		} catch (Exception e) {
			throw new BaseException("create tempalte error. content: " + templateContent + "\n error:" + e.getMessage(), e);
		}
		return template;
	}
}
