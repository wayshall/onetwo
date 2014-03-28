package org.onetwo.common.spring.ftl;

import freemarker.cache.TemplateLoader;


/****
 * 动态模板
 * @author weishao
 *
 */
public class DynamicFreemarkerTemplateConfigurer extends AbstractFreemarkerTemplateConfigurer {

	private StringTemplateProvider templateProvider;

	@Override
	protected TemplateLoader getTempateLoader() {
		return new DynamicTemplateLoader(getTemplateProvider());
	}

	public StringTemplateProvider getTemplateProvider() {
		return templateProvider;
	}

	public void setTemplateProvider(StringTemplateProvider templateProvider) {
		this.templateProvider = templateProvider;
	}
	


}
