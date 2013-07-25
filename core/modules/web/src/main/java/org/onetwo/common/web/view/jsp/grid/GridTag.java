package org.onetwo.common.web.view.jsp.grid;

import javax.servlet.jsp.JspException;

import org.onetwo.common.web.view.jsp.TagUtils;

@SuppressWarnings("serial")
public class GridTag extends BaseGridTag {

	private String template = TagUtils.getViewPage("lib/grid/default-grid.jsp");
	private Object dataSource;
	private int colspan = 0;
	
	@Override
	public int doEndTag() throws JspException {
		try {
			this.pageContext.include(getTemplate());
		} catch (Exception e) {
			throw new JspException("render grid error : " + e.getMessage());
		} 
		return EVAL_PAGE;
	}
	
	@Override
	public int doStartTag() throws JspException {
		GridTagBean grid = new GridTagBean();
		grid.setPage(TagUtils.toPage(dataSource));
		grid.setColspan(colspan);
		
		pageContext.getRequest().setAttribute(getGridVarName(), grid);
		
		return super.doStartTag();
	}

	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = TagUtils.getViewPage(template);
	}

	public void setDataSource(Object dataSource) {
		this.dataSource = dataSource;
	}

	public void setColspan(int colspan) {
		this.colspan = colspan;
	}

}
