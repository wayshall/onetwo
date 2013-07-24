package org.onetwo.plugins.codegen.generator;

import org.onetwo.plugins.codegen.db.TableInfo;


public interface TemplateContextBuilder {
	
	public String getName();
	
	public TemplateContext buildTemplateContext(TableInfo table, GenContext context);
 
}
