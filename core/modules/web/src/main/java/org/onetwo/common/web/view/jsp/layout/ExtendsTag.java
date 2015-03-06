package org.onetwo.common.web.view.jsp.layout;

import javax.servlet.jsp.JspException;

import org.onetwo.common.web.view.jsp.AbstractBodyTag;

@SuppressWarnings("serial")
public class ExtendsTag extends AbstractBodyTag {

	private String parentPage;
	
	@Override
	public int doEndTag() throws JspException {
		String t = getThemeSetting().getLayoutPage(parentPage);
		try {
			renderTemplate(t);
		} catch (JspException e) {
			throw e;
		}  catch (Exception e) {
			throw new JspException("render layout page["+t+"] error : " + e.getMessage());
		} 

		return EVAL_PAGE;
	}

	public void setParentPage(String parentPage) {
		this.parentPage = parentPage;
	}

}
