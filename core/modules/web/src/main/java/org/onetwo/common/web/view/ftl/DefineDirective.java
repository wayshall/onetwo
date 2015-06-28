package org.onetwo.common.web.view.ftl;

import java.io.IOException;
import java.util.Map;

import org.onetwo.common.web.view.ftl.OverrideDirective.OverrideBodyWraper;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@SuppressWarnings("rawtypes")
public class DefineDirective implements TemplateDirectiveModel {

	public static final String DIRECTIVE_NAME = "define";
	public static final String PARAMS_NAME = "name";
	
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		String name = DirectivesUtils.getRequiredParameterByString(params, PARAMS_NAME);
		OverrideBodyWraper override = DirectivesUtils.getOverrideBody(env, name);
		if(override!=null){
			if(override.render)
				return ;
			override.render(env.getOut());
		}else{
			if(body!=null)
				body.render(env.getOut());
		}
	}

}
