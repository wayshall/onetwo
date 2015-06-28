package org.onetwo.plugins.fmtag.directive;

import java.io.IOException;
import java.util.Map;

import org.onetwo.common.web.view.ftl.AbstractDirective;
import org.onetwo.common.web.view.ftl.DirectivesUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@SuppressWarnings("rawtypes")
public class VarDirective extends AbstractDirective implements TemplateDirectiveModel {

	public static final String DIRECTIVE_NAME = "var";
	public static String PARAMS_NAME = "name";
	public static String PARAMS_VALUE = "value";
	public static String PARAMS_NESTED = "nested";
	
	@Override
	public void render(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		String name = DirectivesUtils.getRequiredParameterByString(params, PARAMS_NAME);
		boolean nested = DirectivesUtils.getParameterByBoolean(params, PARAMS_VALUE, false);
		Object val = "";
		if(nested){
			val = getBodyContent(body);
		}else{
			val = DirectivesUtils.getRequiredParameter(params, PARAMS_VALUE);
		}
		DirectivesUtils.setVariable(env, name, val);
	}

	@Override
	public String getDirectiveName() {
		return DIRECTIVE_NAME;
	}

}
