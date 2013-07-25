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
	
	public RowTagBean createDefaultIteratorRow() {
		RowTagBean row = new RowTagBean(RowType.iterator);
		if(!rows.contains(row)){
			row.renderHeader = true;
			addRow(row);
		}
		return row;
	}
	
	public void addRow(RowTagBean row){
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

}
