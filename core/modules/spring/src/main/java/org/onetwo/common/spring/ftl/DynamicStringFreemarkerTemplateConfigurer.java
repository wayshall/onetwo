package org.onetwo.common.spring.ftl;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;

/****
 * 动态模板
 * @author weishao
 *
 */
public class DynamicStringFreemarkerTemplateConfigurer extends DynamicFreemarkerTemplateConfigurer {

	private DynamicTemplateLoader dynamicTemplateLoader;
	

	public DynamicStringFreemarkerTemplateConfigurer(StringTemplateProvider templateProvider) {
		super();
		setTemplateProvider(templateProvider);
		this.dynamicTemplateLoader = new DynamicTemplateLoader(getTemplateProvider());
	}

	public StringTemplateProvider getTemplateProvider() {
		return super.getTemplateProvider();
	}

	@Override
	protected void buildConfigration(Configuration cfg) {
		// 方括号
		cfg.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
	}

	protected TemplateLoader getTempateLoader(){
		return dynamicTemplateLoader;
	}
	

}
