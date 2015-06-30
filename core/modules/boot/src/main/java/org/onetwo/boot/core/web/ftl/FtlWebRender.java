package org.onetwo.boot.core.web.ftl;

import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.WebRender;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.ftl.AbstractFreemarkerTemplateConfigurer;
import org.onetwo.common.spring.ftl.FtlUtils;

import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FtlWebRender implements WebRender {
	
	final private AbstractFreemarkerTemplateConfigurer freemarkerTemplateConfigurer;
	

	public AbstractFreemarkerTemplateConfigurer getFreemarkerTemplateConfigurer() {
		return freemarkerTemplateConfigurer;
	}

	public FtlWebRender(AbstractFreemarkerTemplateConfigurer freemarkerTemplateConfigurer) {
		super();
		this.freemarkerTemplateConfigurer = freemarkerTemplateConfigurer;
	}

	@Override
    public void render(HttpServletResponse response, String name, Object dataModel){
		Template template = freemarkerTemplateConfigurer.getTemplate(name);
		try {
			template.process(dataModel, response.getWriter(), FtlUtils.BEAN_WRAPPER);
		} catch (TemplateException e) {
			throw new BaseException("parse tempalte error : " + name, e);
		} catch (Exception e) {
			throw new BaseException("render error : " + name, e);
		}
	}
}
