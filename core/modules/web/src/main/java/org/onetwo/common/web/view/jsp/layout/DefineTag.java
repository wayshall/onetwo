package org.onetwo.common.web.view.jsp.layout;

import java.io.IOException;

import javax.servlet.jsp.JspException;

@SuppressWarnings("serial")
public class DefineTag extends BaseLayoutTag {

	private String name;
	
	@Override
	public int doEndTag() throws JspException {
		if(hasChildPageOverride(name)){
			String text = getChildPageOverrideBody(name).getContentText();
			try {
				pageContext.getOut().write(text);
			} catch (IOException e) {
				throw new JspException("render override["+name+"] error : " + e.getMessage());
			}
		}
		return EVAL_PAGE;
	}

	@Override
	public int doStartTag() throws JspException {
		if(hasChildPageOverride(name)){
			return SKIP_BODY;
		}else{
			//there are not override
			return EVAL_BODY_INCLUDE;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
