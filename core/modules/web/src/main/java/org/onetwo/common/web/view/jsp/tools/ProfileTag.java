package org.onetwo.common.web.view.jsp.tools;

import javax.servlet.jsp.JspException;

import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.web.view.jsp.AbstractBodyTag;

@SuppressWarnings("serial")
public class ProfileTag extends AbstractBodyTag {
	private static final String VAR_NAME = "prifle_active";
	
	private boolean active = true;
	private String name;
	
	@Override
	public int doStartTag() throws JspException {
		boolean oldValue = UtilTimerStack.isActive();
		setComponentIntoRequest(VAR_NAME, oldValue);
		UtilTimerStack.setActive(active);
		UtilTimerStack.push(name);
		return EVAL_BODY_INCLUDE;
	}
	@Override
	public int doEndTag() throws JspException {
		UtilTimerStack.pop(name);
		boolean oldValue = getComponentFromRequest(VAR_NAME, Boolean.class);
		this.clearComponentFromRequest(VAR_NAME);
		UtilTimerStack.setActive(oldValue);
		return EVAL_PAGE;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
