package org.onetwo.plugins.fmtagext.ui;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.fish.exception.JFishException;
import org.onetwo.common.utils.NoResultBlock;
import org.onetwo.common.web.view.ftl.DirectivesUtils;
import org.springframework.web.servlet.support.RequestContext;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateModel;

public class UIRender {
	public static final String ENCODING = "UTF-8";
	public static final String THIS_COMPONENT = "__this__";

	private final Environment env;
	private final TemplateDirectiveBody body;
	private final Object data;
	
	public UIRender(Environment env, TemplateDirectiveBody body, Object data) {
		super();
		this.env = env;
		this.body = body;
		this.data = data;
	}
	

	public void setObjectVariable(String name, Object val){
		DirectivesUtils.setObjectVariable(env, name, val);
	}


	protected TemplateModel getVariable(String varName){
		return DirectivesUtils.getVariable(env, varName, true);
	}
	
	public RequestContext getRequestContext(){
		RequestContext request = DirectivesUtils.getRequestContext(env);
		return request;
	}
	
	public HttpServletRequest getRequest(){
		HttpServletRequest request = DirectivesUtils.getRequest(env);
		return request;
	}
	
	public Object getThis(){
		return getVariable(THIS_COMPONENT);
	}
	
	public void setThis(Object dataComponent){
		setObjectVariable(THIS_COMPONENT, dataComponent);
	}
	
	public void renderUIComponent(UIComponent component){
		Object oldThis = getThis();
		
		DataUIComponent dataComponent = null;
		if(!DataUIComponent.class.isInstance(component)){
			dataComponent = new SimpleDataComponent(component, data);
		}else{
			dataComponent = (DataUIComponent) component;
		}
		
		setThis(dataComponent);
		try {
//			renderTemplate(component.getComponentTemplate());
			env.include(component.getComponentTemplate(), ENCODING, true);
		} catch (Exception e) {
			throw new JFishException("render uicomponent["+component+"] error : " + e.getMessage(), e);
		}
		setThis(oldThis);
	}
//	private void renderTemplate(String template){
//		try {
////			System.out.println("render:" + template);
//			env.include(template, ENCODING, true);
//		} catch (Exception e) {
//			throw new JFishException("renderTemplate error : " + template, e);
//		} 
//	}
	
	public void renderString(String str){
		try {
			this.env.getOut().write(str);
		} catch (IOException e) {
			throw new JFishException("renderString error : " + e.getMessage(), e);
		}
	}
	
	public void render(NoResultBlock<Writer> block){
		try {
			block.execute(this.env.getOut());
		} catch (Exception e) {
			throw new JFishException("render error : " + e.getMessage(), e);
		}
	}
	
	public void renderBody(Writer out){
		try {
			body.render(out);
		} catch (Exception e) {
			throw new JFishException("renderBody error : " + e.getMessage(), e);
		}
	}
	
	public String renderBodyAsString(){
		try {
			StringWriter out = new StringWriter();
			body.render(out);
			return out.toString();
		} catch (Exception e) {
			throw new JFishException("renderBodyAsString error : " + e.getMessage(), e);
		}
	}


	public Object getData() {
		return data;
	}

}
