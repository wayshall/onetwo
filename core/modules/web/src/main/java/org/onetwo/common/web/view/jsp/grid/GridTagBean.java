package org.onetwo.common.web.view.jsp.grid;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.utils.Page;
import org.onetwo.common.web.view.HtmlElement;
import org.onetwo.common.web.view.jsp.grid.RowTagBean.RowType;

public class GridTagBean extends HtmlElement {
	
	private Page<?> page;
	private List<RowTagBean> rows = new ArrayList<RowTagBean>();
	private int colspan = 0;
	

	private String action;
	private String queryString;

	private boolean toolbar;
	
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

}
