package org.onetwo.common.web.s2.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import org.onetwo.common.web.s2.tag.component.AjaxTable;
import org.onetwo.common.web.s2.tag.component.StrutsTable;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings({"serial"})
public class StrutsTableTag extends AbstractTableTag{

	private String dataSource;
//	private String var;
	private String colCount;
	protected String action;
	protected String dataFormat;
	
	
	protected boolean skipIfNotIncluded;
	protected boolean ajax = true;
	
	public StrutsTableTag(){
	}
	
	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		if(ajax)
			return new AjaxTable(stack, req, res);
		else
			return new StrutsTable(stack, req, res);
	}

	@Override
	protected void populateParams() {
		super.populateParams();
		StrutsTable table = this.getComponent();
		table.setDataSource(dataSource);
		table.setColCount(colCount);
		table.setAction(action);
		table.setAjax(ajax);
		table.setSkipIfNotIncluded(skipIfNotIncluded);
		table.setDataFormat(dataFormat);
	}
	
	public StrutsTable getComponent(){
		return (StrutsTable) this.component;
	}
	
	@StrutsTagAttribute(description="setDataSource", type="String", defaultValue="page")
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public void setColCount(String colCount) {
		this.colCount = colCount;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setSkipIfNotIncluded(boolean skipIfNotIncluded) {
		this.skipIfNotIncluded = skipIfNotIncluded;
	}

	public void setAjax(boolean ajax) {
		this.ajax = ajax;
	}

	public void setParams(String params) {
		super.setParams(params);
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

}
