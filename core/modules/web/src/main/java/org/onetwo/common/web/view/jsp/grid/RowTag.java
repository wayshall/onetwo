package org.onetwo.common.web.view.jsp.grid;

import javax.servlet.jsp.JspException;

import org.onetwo.common.web.view.jsp.TagUtils;

@SuppressWarnings("serial")
public class RowTag extends BaseGridTag {

	private String template = TagUtils.getViewPage("lib/grid/default-grid.jsp");a
	@Override
	public int doEndTag() throws JspException {
		try {
			this.pageContext.include(getTemplate());
		} catch (Exception e) {
			throw new JspException("render grid error : " + e.getMessage());
		} 
		return EVAL_PAGE;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = TagUtils.getViewPage(template);
	}

}
