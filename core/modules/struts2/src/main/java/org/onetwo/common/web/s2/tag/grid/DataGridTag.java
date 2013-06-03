package org.onetwo.common.web.s2.tag.grid;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.annotations.StrutsTagAttribute;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings({"serial"})
public class DataGridTag extends AbstractDataGridTag{

	private String dataSource;
	protected String action;
	
	protected boolean ajax = true;
	protected String pagination;
	protected String paginationTemplate;
	
	protected boolean hasForm = true;
	protected boolean includeCommonToolbar;
	
	protected String var;
	
	protected String condition;
	
	private String pageParamPrefix;
	
	public DataGridTag(){
	}
	
	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new DataGrid(stack, req, res);
	}

	@Override
	protected void populateParams() {
		super.populateParams();
		DataGrid grid = this.getComponent();
		grid.setDataSource(dataSource);
		grid.setAction(action);
		grid.setAjax(ajax);
		grid.setPagination(pagination);
		grid.setHasForm(hasForm);
		grid.setVar(var);
		grid.setIncludeCommonToolbar(includeCommonToolbar);
		grid.setCondition(condition);
		grid.setPageParamPrefix(pageParamPrefix);
	}
	
	public DataGrid getComponent(){
		return (DataGrid) this.component;
	}
	
	@StrutsTagAttribute(description="setDataSource", type="String", defaultValue="page")
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}


	public void setAction(String action) {
		this.action = action;
	}

	public void setAjax(boolean ajax) {
		this.ajax = ajax;
	}

	public void setParams(String params) {
		super.setParams(params);
	}

	public void setPagination(String pagination) {
		this.pagination = pagination;
	}

	public void setPaginationTemplate(String paginationTemplate) {
		this.paginationTemplate = paginationTemplate;
	}

	public void setHasForm(boolean hasForm) {
		this.hasForm = hasForm;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public void setIncludeCommonToolbar(boolean includeCommonToolbar) {
		this.includeCommonToolbar = includeCommonToolbar;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public void setPageParamPrefix(String pageParamPrefix) {
		this.pageParamPrefix = pageParamPrefix;
	}

}
