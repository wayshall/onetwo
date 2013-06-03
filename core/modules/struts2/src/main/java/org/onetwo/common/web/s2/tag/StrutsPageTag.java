package org.onetwo.common.web.s2.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.onetwo.common.web.s2.tag.component.StrutsPage;

import com.opensymphony.xwork2.util.ValueStack;

public class StrutsPageTag extends AbstractTableTag {

	private String form;
	
	@Override
	public Component getBean(ValueStack stack, HttpServletRequest request,
			HttpServletResponse response) {
		return new StrutsPage(stack, request, response);
	}
	
	@Override
	protected void populateParams() {
		super.populateParams();
		StrutsPage page = (StrutsPage)component;
		page.setForm(form);
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}
}
