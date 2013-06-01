package org.onetwo.plugins.codegen.generator;

import org.onetwo.common.fish.orm.TableInfo;


public interface TemplateContextBuilder {
	
	public String getName();
	
	public TemplateContext buildTemplateContext(TableInfo table, GenContext context);
 
}
