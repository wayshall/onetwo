package org.onetwo.common.web.view.jsp.layout;

import javax.servlet.jsp.JspException;

import org.onetwo.common.web.view.jsp.TagUtils;

@SuppressWarnings("serial")
public class ExtendsTag extends BaseLayoutTag {

	private String parentPage = TagUtils.getViewPage("layout/application.jsp");
	
	@Override
	public int doEndTag() throws JspException {
		try {
			this.pageContext.include(getParentPage());
		} catch (Exception e) {
			throw new JspException("render layout page["+parentPage+"] error : " + e.getMessage());
		} 

		return EVAL_PAGE;
	}


	public String getParentPage() {
		return parentPage;
	}

	public void setParentPage(String parentPage) {
		this.parentPage = TagUtils.getViewPage(parentPage);
	}

}
