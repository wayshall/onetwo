package org.onetwo.common.web.view.ftl;

import java.io.IOException;
import java.util.Map;

import org.onetwo.common.profiling.UtilTimerStack;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@SuppressWarnings("rawtypes")
public class ProfileDirective implements TemplateDirectiveModel {

	public static final String DIRECTIVE_NAME = "profile";
	public static final String PARAMS_ACTIVE = "active";
	
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		if(params.containsKey(PARAMS_ACTIVE)){
			boolean active = DirectivesUtils.getParameterByString(params, PARAMS_ACTIVE, "false").equals("true");
			UtilTimerStack.setActive(active);
		}
		String name = DirectivesUtils.getRequiredParameterByString(params, "name");
		UtilTimerStack.push(name);
		body.render(env.getOut());
		UtilTimerStack.pop(name);
	}

}
