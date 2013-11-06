package org.onetwo.common.web.view.jsp.grid;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.HtmlElement;
import org.onetwo.common.web.view.jsp.datagrid.PaginationType;
import org.onetwo.common.web.view.jsp.datagrid.SearchForm;
import org.onetwo.common.web.view.jsp.grid.RowTagBean.RowType;

public class GridTagBean extends HtmlElement {
	
	private Page<?> page;
	private List<RowTagBean> rows = new ArrayList<RowTagBean>();
	private int colspan = 0;
	

	private String action;
	private String queryString;

	private boolean toolbar;
	private boolean generatedForm;
	
	private String bodyContent;

	private boolean pagination;
	private PaginationType paginationType;
	
	private SearchForm searchFormBean;
	private boolean searchForm;
	

	private boolean ajaxSupported = false;
	private String ajaxZoneName;
//	private String ajaxInstName;
	
	public RowTagBean createDefaultIteratorRow() {
		RowTagBean row = new RowTagBean(RowType.iterator);
		if(!rows.contains(row)){
			row.setRenderHeader(true);
			addRow(row);
		}
		return row;
	}
	
	public void addRow(RowTagBean row){
		row.setGridTagBean(this);
		this.rows.add(row);
	}
	public RowTagBean getCurrentRow(){
		return rows.get(rows.size()-1);
	}
	public Page<?> getPage() {
		return page;
	}
	public List<RowTagBean> getRows() {
		return rows;
	}
	public int getColspan() {
		if(colspan<1 && !rows.isEmpty()){
			this.colspan = this.rows.get(0).getFields().size();
		}
		return colspan;
	}

	public void setPage(Page<?> page) {
		this.page = page;
	}

	public void setColspan(int colspan) {
		this.colspan = colspan;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	
	public String getActionWithQueryString(){
		if(StringUtils.isBlank(queryString))
			return action;
		String link = action;
		if(action.contains("?")){
			link += "&" + queryString;
		}else{
			link += "?" + queryString;
		}
		return link;
	}

	public boolean isToolbar() {
		return toolbar;
	}

	public void setToolbar(boolean toolbar) {
		this.toolbar = toolbar;
	}

	public String getBodyContent() {
		return bodyContent;
	}

	public void setBodyContent(String bodyContent) {
		this.bodyContent = bodyContent;
	}
	
	public String getFormId(){
		return getId() + "Form";
	}

	public PaginationType getPaginationType() {
		return paginationType;
	}

	public void setPaginationType(PaginationType paginationType) {
		this.paginationType = paginationType;
		if(isNonePagination())
			this.pagination = false;
	}
	
	public boolean isNonePagination(){
		return this.paginationType==PaginationType.none;
	}
	
	public boolean isFormPagination(){
		return this.paginationType==PaginationType.form;
	}

	public boolean isGeneratedForm() {
		return generatedForm;
	}

	public void setGeneratedForm(boolean generatedForm) {
		this.generatedForm = generatedForm;
	}

	public void setPagination(boolean pagination) {
		this.pagination = pagination;
	}
	
	public boolean isPagination(){
		if(!this.page.isAutoCount())
			return false;
		return this.pagination;
	}

	public void setSearchForm(boolean searchForm) {
		this.searchForm = searchForm;
		if(searchForm){
			searchFormBean = new SearchForm();
		}
	}

	public SearchForm getSearchFormBean() {
		return searchFormBean;
	}

	public boolean isSearchForm() {
		return searchForm;
	}

	public void addSearchField(FieldTagBean field){
		if(!searchForm)
			return ;
		getSearchFormBean().addField(field);
	}

	public boolean isAjaxSupported() {
		return ajaxSupported;
	}

	public void setAjaxSupported(boolean ajaxSupported) {
		this.ajaxSupported = ajaxSupported;
	}

	public String getAjaxZoneName() {
		return ajaxZoneName;
	}

	public void setAjaxZoneName(String ajaxZoneName) {
		this.ajaxZoneName = ajaxZoneName;
	}


	
}
