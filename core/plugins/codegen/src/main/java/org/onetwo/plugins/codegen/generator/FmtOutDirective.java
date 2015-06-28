package org.onetwo.plugins.codegen.generator;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.onetwo.common.web.view.ftl.AbstractDirective;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class FmtOutDirective extends AbstractDirective {
	
	public static final String DIRECTIVE_NAME = "tag";

	@Override
	public String getDirectiveName() {
		return DIRECTIVE_NAME;
	}

	@Override
	public void render(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
//		String name = getRequiredParameterByString(params, "name");
		Writer wr = env.getOut();
		body.render(wr);
	}

}
