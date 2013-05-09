package org.onetwo.common.web.s2.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.struts2.components.Component;
import org.onetwo.common.web.s2.tag.component.StrutsColumn;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("serial")
public class StrutsColumnTag extends AbstractTableTag{
	
	protected boolean autoValue;
	protected String sortable;

	protected int colspan;
	protected int rowspan;

	protected String dataType;
	
	protected String dataFormat;
	
	protected String link;
	
	public StrutsColumnTag(){
		this.autoValue = true;
		this.colspan = 1;
		this.rowspan = 1;
	}
	
	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new StrutsColumn(stack, req, res);
	}

	@Override
	protected void populateParams() {
		super.populateParams();
		StrutsColumn col = (StrutsColumn) component;
		col.setAutoValue(autoValue);
		col.setSortable(sortable);
		col.setColspan(colspan);
		col.setRowspan(rowspan);
		col.setDataType(dataType);
		col.setDataFormat(dataFormat);
		col.setLink(link);
	}

    public int doEndTag() throws JspException {
        return super.doEndTag();
    }

	public void setAutoValue(boolean autoValue) {
		this.autoValue = autoValue;
	}

	public void setSortable(String sortable) {
		this.sortable = sortable;
	}

	public void setColspan(int colspan) {
		this.colspan = colspan;
	}

	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
