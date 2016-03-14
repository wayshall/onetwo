package org.onetwo.common.web.view.ftl;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@SuppressWarnings("rawtypes")
public class OverrideDirective extends AbstractDirective implements TemplateDirectiveModel {

	public static final String DIRECTIVE_NAME = "override";
	public static String PARAMS_NAME = "name";
	
	static class OverrideBodyWraper implements TemplateDirectiveBody, TemplateModel {

		private TemplateDirectiveBody body;
		OverrideBodyWraper parentBody;
//		private Environment env;
		String name;
		boolean render;
		
		public OverrideBodyWraper(String name, TemplateDirectiveBody body) {
			super();
			this.name = name;
			this.body = body;
//			this.env = env;
			this.render = false;
		}

		@Override
		public void render(Writer out) throws TemplateException, IOException {
			if(body==null)
				return ;
			this.body.render(out);
			this.render = true;
		}
		
	}
	
	@Override
	public void render(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		String name = DirectivesUtils.getRequiredParameterByString(params, PARAMS_NAME);
//		DirectivesUtils.setVariable(env, "active", "test");
		OverrideBodyWraper preOverride = DirectivesUtils.getOverrideBody(env, name);
		OverrideBodyWraper curOverride = new OverrideBodyWraper(name, body);
		if(preOverride==null){
			DirectivesUtils.setOverrideBody(env, name, curOverride);
		}else{
			preOverride.parentBody = curOverride;
		}
	}

	@Override
	public String getDirectiveName() {
		return DIRECTIVE_NAME;
	}

}
