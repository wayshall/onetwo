package org.onetwo.common.db.filequery.func;

import java.util.List;

import org.onetwo.common.utils.StringUtils;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

abstract public class SQLFunctionGenerator implements TemplateMethodModel {

	@Override
	public Object exec(List arguments) throws TemplateModelException {
		String val = render(arguments);
		return StringUtils.emptyIfNull(val);
	}
	

	abstract protected String render(List<String> arguments) ;
	
	
}
