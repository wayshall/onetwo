package org.onetwo.common.excel;

import java.util.List;

@SuppressWarnings("unchecked")
public class TemplateModel {
	
	private String name;
	
	private List<RowModel> rows;
	
	public TemplateModel(){
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<RowModel> getRows() {
		return rows;
	}

	public void setRows(List<RowModel> rows) {
		this.rows = rows;
	} 

}
