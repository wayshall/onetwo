package org.onetwo.common.web.s2.tag.template;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.onetwo.common.web.s2.tag.WebUIClosingTag;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("serial")
public class PageTag extends WebUIClosingTag {
	
	private String _extends;
	
	private boolean ignoreIfNotTemplate;
	
	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new Page(stack, req, res);
	}

	protected void populateParams(){
		super.populateParams();
		
		Page def = (Page) getComponent();
		def.setExtends(_extends);
		def.setIgnoreIfNotTemplate(ignoreIfNotTemplate);
	}

	public void setExtends(String _extends) {
		this._extends = _extends;
	}

	public void setIgnoreIfNotTemplate(boolean ignoreIfNotTemplate) {
		this.ignoreIfNotTemplate = ignoreIfNotTemplate;
	}

}
