package org.onetwo.plugins.fmtag.directive;

import java.io.IOException;
import java.util.Map;

import org.onetwo.common.ftl.directive.AbstractDirective;
import org.onetwo.common.ftl.directive.DirectivesUtils;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.LangUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@SuppressWarnings("rawtypes")
public class TokenDirective extends AbstractDirective implements TemplateDirectiveModel {

	public static final String DIRECTIVE_NAME = "token";
	public static String PARAMS_NAME = "name";
	
	@Override
	public void render(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		String name = DirectivesUtils.getParameterByString(params, PARAMS_NAME, JFishWebUtils.DEFAULT_TOKEN_NAME);
		String tokenValue = LangUtils.generateToken(name);
		JFishWebUtils.session(name, tokenValue);
		env.getOut().write("<input name='"+JFishWebUtils.DEFAULT_TOKEN_FIELD_NAME+"' type='hidden' value='"+name+"'/>");
		env.getOut().write("<input name='"+name+"' type='hidden' value='"+tokenValue+"'/>");
	}

	@Override
	public String getDirectiveName() {
		return DIRECTIVE_NAME;
	}

}
