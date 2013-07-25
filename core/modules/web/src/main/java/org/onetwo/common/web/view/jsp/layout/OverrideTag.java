package org.onetwo.common.web.view.jsp.layout;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

@SuppressWarnings("serial")
public class OverrideTag extends BaseLayoutTag {
	
	private String name;

	@Override
	public int doEndTag() throws JspException {
		if(hasChildPageOverride(name))
			return EVAL_PAGE;
		
		BodyContent content = getBodyContent();
		OverrideBody override = new OverrideBody(name, content);
		this.pageContext.getRequest().setAttribute(getOverrideName(name), override);
		
		return EVAL_PAGE;
	}

	@Override
	public int doStartTag() throws JspException {
		return hasChildPageOverride(name)?SKIP_BODY:EVAL_BODY_BUFFERED;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
