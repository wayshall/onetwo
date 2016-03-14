package org.onetwo.common.web.view.ftl;

import java.io.IOException;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@SuppressWarnings("rawtypes")
public class ExtendsDirective implements TemplateDirectiveModel {
	
	public static final String DIRECTIVE_NAME = "extends";

	public static final String PARAMS_FILE = "parent";
	public static final String DEFAULT_LAYOUT_DIR = "/layout/";
	public static final String DEFAULT_LAYOUT = "application.ftl";
	public static final String FTL = ".ftl";

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		String file = DEFAULT_LAYOUT_DIR+DirectivesUtils.getParameterByString(params, PARAMS_FILE, DEFAULT_LAYOUT);
		String template = env.toFullTemplateName("/", file);
		if(!template.contains(".")){
			template += FTL;
		}
		body.render(env.getOut());
		env.include(template, DirectivesUtils.ENCODING, true);
	}

}
