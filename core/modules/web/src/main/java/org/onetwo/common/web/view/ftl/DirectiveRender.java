package org.onetwo.common.web.view.ftl;

import org.onetwo.common.utils.LangUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;

public class DirectiveRender {

	private String tag;
	private Environment env;
	private TemplateDirectiveBody body;
	
	public DirectiveRender(String tag, Environment env, TemplateDirectiveBody body) {
		super();
		this.tag = tag;
		this.env = env;
		this.body = body;
	}
	public Environment getEnv() {
		return env;
	}

	public TemplateDirectiveBody getBody() {
		return body;
	}

	public void render(){
		if(body==null || env==null)
			return ;
		try {
			body.render(env.getOut());
		} catch (Exception e) {
			LangUtils.throwBaseException("render tempalte["+tag+"] error : "+e.getMessage(), e);
		} 
	}
	
}
