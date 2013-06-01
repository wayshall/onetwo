package org.onetwo.common.web.s2.tag.template;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.onetwo.common.web.s2.tag.WebUIClosingTag;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("serial")
public class ReplaceTag extends WebUIClosingTag {
	
	protected String condition;
	
	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new Replace(stack, req, res);
	}

	protected void populateParams(){
		super.populateParams();
		Replace def = (Replace) getComponent();
		def.setName(name);
		def.setCondition(condition);
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

}
