package org.onetwo.common.spring.ftl;

import java.io.StringWriter;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;

import freemarker.template.Template;
import freemarker.template.TemplateException;

public class DefaultTemplateParser implements TemplateParser {
	
	final private AbstractFreemarkerTemplateConfigurer freemarkerTemplateConfigurer;
	
	public DefaultTemplateParser(
			AbstractFreemarkerTemplateConfigurer freemarkerTemplateConfigurer) {
		super();
		this.freemarkerTemplateConfigurer = freemarkerTemplateConfigurer;
	}

	public String parse(String name, Object rootMap){
		Template template = freemarkerTemplateConfigurer.getTemplate(name);
		StringWriter sw = new StringWriter();
		try {
			template.process(rootMap, sw);
		} catch (TemplateException e) {
			e.printStackTrace();
			Throwable cause = e.getCause();
			if(cause!=null){
				throw LangUtils.asBaseException("parse tempalte error : " + cause.getMessage(), cause);
			}else{
				throw new BaseException("parse tempalte error : " + e.getMessage(), e);
			}
		} catch (Exception e) {
			throw new BaseException("parse tempalte error : " + e.getMessage(), e);
		}
		return sw.toString();
	}

}
