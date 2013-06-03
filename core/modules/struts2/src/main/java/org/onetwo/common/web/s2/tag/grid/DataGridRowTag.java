package org.onetwo.common.web.s2.tag.grid;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.struts2.components.Component;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("serial")
public class DataGridRowTag extends AbstractDataGridTag {
	
	private String type;
	protected boolean renderHeader = true;
	
	public DataGridRowTag(){
	}
	
	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new DataGridRow(stack, req, res);
	}

	@Override
	protected void populateParams() {
		super.populateParams();
		DataGridRow row = (DataGridRow) getComponent();
		row.setTypeString(type);
		row.setRenderHeader(renderHeader);
	}

    public int doEndTag() throws JspException {
        return super.doEndTag();
    }

	public void setType(String type) {
		this.type = type;
	}

	public boolean isRenderHeader() {
		return renderHeader;
	}

	public void setRenderHeader(boolean renderHeader) {
		this.renderHeader = renderHeader;
	}


}
