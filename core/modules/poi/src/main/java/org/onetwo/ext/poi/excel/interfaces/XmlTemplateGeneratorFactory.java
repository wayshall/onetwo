package org.onetwo.ext.poi.excel.interfaces;

import java.util.Map;


public interface XmlTemplateGeneratorFactory {

	public TemplateGenerator create(String template, Map<String, Object> context);
	public boolean checkTemplate(String template);
//	public void setBaseTemplateDir(String baseTemplateDir);
//	public void setCacheTemplate(boolean cacheTemplate);
}
