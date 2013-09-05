package org.onetwo.common.spring.ftl;

import java.io.IOException;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.CharsetUtils;
import org.onetwo.common.utils.LangUtils;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

abstract public class DynamicFreemarkerTemplate{
	public static final BeansWrapper INSTANCE = new BeansWrapper();

	static {
		INSTANCE.setSimpleMapWrapper(true);
	}
	
	private Configuration configuration;
	
	private String encoding = CharsetUtils.UTF_8;
	
//	private String templateDir;
	
	private StringTemplateProvider templateProvider;
	
	public DynamicFreemarkerTemplate(){
	}

	protected BeansWrapper getBeansWrapper(){
		return INSTANCE;
	}
	
	/****
	 * must be invoke after contruction
	 */
	public void initialize() {
		Assert.notNull(templateProvider);
		try {
			Map<String, Object> freemarkerVariables = LangUtils.newHashMap();
			
			this.configuration = new Configuration();
			this.configuration.setObjectWrapper(getBeansWrapper());
			this.configuration.setOutputEncoding(this.encoding);
//			this.cfg.setDirectoryForTemplateLoading(new File(templateDir));
			this.configuration.setTemplateLoader(new DynamicTemplateLoader(templateProvider));
			configuration.setAllSharedVariables(new SimpleHash(freemarkerVariables, configuration.getObjectWrapper()));
			this.buildConfigration(this.configuration);
		} catch (Exception e) {
			throw new BaseException("create freemarker template error : " + e.getMessage(), e);
		}
	}
	protected void buildConfigration(Configuration cfg) {
	}
	
	protected Template getTemplate(String name){
		Template template;
		try {
			template = getConfiguration().getTemplate(name);
		} catch (IOException e) {
			throw new BaseException("get tempalte error : " + e.getMessage(), e);
		}
		return template;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setTemplateProvider(StringTemplateProvider templateProvider) {
		this.templateProvider = templateProvider;
	}

	public StringTemplateProvider getTemplateProvider() {
		return templateProvider;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public String getEncoding() {
		return encoding;
	}


}
