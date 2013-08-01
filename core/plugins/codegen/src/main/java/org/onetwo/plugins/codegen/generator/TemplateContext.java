package org.onetwo.plugins.codegen.generator;

import java.util.Map;

public interface TemplateContext {
	
	public String getOutfile();
	
	public String getTemplate();
	
	@SuppressWarnings("rawtypes")
	public Map getContext();
	
	public Object get(String key);
	
}
