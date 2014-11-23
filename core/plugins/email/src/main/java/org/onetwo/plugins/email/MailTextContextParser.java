package org.onetwo.plugins.email;

import javax.annotation.Resource;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.ftl.StringFtlTemplateLoader;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.encrypt.MDFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class MailTextContextParser {

	private static final String DEFAULT_ENCODING = "utf-8";
	
	@Resource
	private Configuration configuration;
	private String encoding = DEFAULT_ENCODING;
	@Resource
	private StringFtlTemplateLoader stringFtlTemplateLoader;

	public String parseContent(MailInfo mailInfo) {
		String content = "";
		/*if(mailInfo.isTemplate()){
			content = generateContent(mailInfo.getContent(), mailInfo.getTemplateContext());
		}else{
			content = mailInfo.getContent();
		}*/
		Assert.notNull(mailInfo.getEmailTextType());
		Template template = null;
		try {
			switch (mailInfo.getEmailTextType()) {
				case STATIC_TEXT:
					content = mailInfo.getContent();
					break;
					
				case TEMPLATE_PATH:
					template = this.configuration.getTemplate(mailInfo.getContent(), encoding);
					content = FreeMarkerTemplateUtils.processTemplateIntoString(template, mailInfo.getTemplateContext());
					break;
					
				case TEMPLATE:
	//				String name = "st-" + String.valueOf(mailInfo.getContent().hashCode());
					String name = "st-" + MDFactory.MD5.encrypt(mailInfo.getContent());
					this.stringFtlTemplateLoader.putTemplate(name, mailInfo.getContent());
					template = this.configuration.getTemplate(name, encoding);
					content = FreeMarkerTemplateUtils.processTemplateIntoString(template, mailInfo.getTemplateContext());
					break;
		
				default:
					break;
			}
		} catch (Exception e) {
			throw new BaseException("parse email text content error: " +e.getMessage(), e);
		}
		
		return content;
	}
	

	public Configuration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setStringFtlTemplateLoader(StringFtlTemplateLoader stringFtlTemplateLoader) {
		this.stringFtlTemplateLoader = stringFtlTemplateLoader;
	}
}
