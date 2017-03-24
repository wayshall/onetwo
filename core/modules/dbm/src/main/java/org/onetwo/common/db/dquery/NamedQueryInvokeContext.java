package org.onetwo.common.db.dquery;

import java.util.Map;

import org.onetwo.common.db.filequery.spi.QueryProvideManager;
import org.onetwo.common.spring.ftl.TemplateParser;

public interface NamedQueryInvokeContext {

	public String getQueryName();
	
	public Map<Object, Object> getParsedParams();
	
	public TemplateParser getParser();

	public void setParser(TemplateParser parser);
	
	public DynamicMethod getDynamicMethod();
	
	public QueryProvideManager getQueryProvideManager();
	
}