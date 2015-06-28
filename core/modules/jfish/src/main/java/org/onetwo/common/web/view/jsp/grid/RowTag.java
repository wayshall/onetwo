package org.onetwo.common.web.view.jsp.grid;

import javax.servlet.jsp.JspException;

import org.onetwo.common.web.view.jsp.grid.RowTagBean.RowType;

@SuppressWarnings("serial")
public class RowTag extends BaseGridTag<RowTagBean> {

	private RowType type = RowType.row;
	private boolean renderHeader;
	
	
	@Override
	public RowTagBean createComponent() {
		return new RowTagBean(type);
	}
	
	protected void populateComponent() throws JspException{
		super.populateComponent();
		GridTagBean grid = getComponentFromRequest(getGridVarName(), GridTagBean.class);
		if(grid==null)
			throw new JspException("row tag must nested in a grid tag.");
		
		component.setRenderHeader(renderHeader);
		grid.addRow(component);
		
		setComponentIntoRequest(getRowVarName(), component);
	}

	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public void setType(String type) {
		this.type = RowType.valueOf(type);
	}

	public boolean isRenderHeader() {
		return renderHeader;
	}

	public void setRenderHeader(boolean renderHeader) {
		this.renderHeader = renderHeader;
	}

}
