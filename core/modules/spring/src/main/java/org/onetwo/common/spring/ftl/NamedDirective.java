package org.onetwo.common.spring.ftl;

import freemarker.template.TemplateDirectiveModel;

public interface NamedDirective extends TemplateDirectiveModel {
	
	String getName();

}
