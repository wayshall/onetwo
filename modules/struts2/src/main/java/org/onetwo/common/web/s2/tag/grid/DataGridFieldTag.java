package org.onetwo.common.web.s2.tag.grid;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.struts2.components.Component;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("serial")
public class DataGridFieldTag extends AbstractDataGridTag {
	
	protected boolean autoValue;
	protected boolean sortable;

//	protected String dataType;
	protected String dataFormat;
	protected String defaultValue;
	protected String link;
	
	protected String valueFetcher;
	protected String type;
	
	protected String condition;
	
	public DataGridFieldTag(){
		this.autoValue = true;
	}
	
	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new DataGridField(stack, req, res);
	}

	@Override
	protected void populateParams() {
		super.populateParams();
		DataGridField field = (DataGridField) component;
		field.setSortable(sortable);
//		col.setDataType(dataType);
		field.setDataFormat(dataFormat);
		field.setLink(link);
		field.setDefaultValue(defaultValue);
		field.setValueFetcher(valueFetcher);
		field.setType(type);
		
		field.setCondition(condition);
	}

    public int doEndTag() throws JspException {
        return super.doEndTag();
    }

	/*public void setDataType(String dataType) {
		this.dataType = dataType;
	}*/

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setValueFetcher(String valueFetcher) {
		this.valueFetcher = valueFetcher;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

}
