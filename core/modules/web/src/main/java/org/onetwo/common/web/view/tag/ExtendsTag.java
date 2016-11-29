package org.onetwo.common.web.view.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

@SuppressWarnings("serial")
public class ExtendsTag extends BodyTagSupport {

	private String parentPage = "/WEB-INF/layout/application.jsp";
	
	@Override
	public int doEndTag() throws JspException {
		try {
			renderTemplate(parentPage);
		} catch (JspException e) {
			throw e;
		}  catch (Exception e) {
			throw new JspException("render layout page["+parentPage+"] error : " + e.getMessage());
		} 

		return EVAL_PAGE;
	}

	public void setParentPage(String parentPage) {
		this.parentPage = parentPage;
	}
	
	protected void renderTemplate(String template) throws JspException{
		try {
			this.pageContext.include(template);
		} catch (Exception e) {
			throw new JspException("render template["+template+"] error : \n " + e.getMessage(), e);
		}
	}

}
