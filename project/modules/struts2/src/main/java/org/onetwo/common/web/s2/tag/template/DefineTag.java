package org.onetwo.common.web.s2.tag.template;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.onetwo.common.web.s2.tag.WebUIClosingTag;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("serial")
public class DefineTag extends WebUIClosingTag {
	
	private String defaultPage;
	
	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new Define(stack, req, res);
	}

	protected void populateParams(){
		super.populateParams();
		
		Define def = (Define) getComponent();
		def.setDefaultPage(defaultPage);
	}

	public void setDefaultPage(String defaultPage) {
		this.defaultPage = defaultPage;
	}


}
