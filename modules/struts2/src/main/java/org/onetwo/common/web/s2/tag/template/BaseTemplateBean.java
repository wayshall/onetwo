package org.onetwo.common.web.s2.tag.template;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.web.s2.tag.WebUIClosingBean;

import com.opensymphony.xwork2.util.ValueStack;

abstract public class BaseTemplateBean extends WebUIClosingBean {
	
	public static final String TEMPLATE_NAME_KEY = "__TEMPLATE_TAG__";

	public BaseTemplateBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
	}
	
	protected String getBodyInContext(String name){
		return (String)getStack().getContext().get(getTemplateNameKey(name));
	}
	
	protected void putBodyInContext(String name, String bodyContent){
		getStack().getContext().put(getTemplateNameKey(name), bodyContent);
	}
	
	protected String getTemplateNameKey(String name){
		return TEMPLATE_NAME_KEY + name;
	}

}
