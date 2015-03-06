package org.onetwo.common.excel.etemplate;

import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.onetwo.common.utils.LangUtils;

public class RowForeachDirectiveModel {

	final private String directiveStart;
	final private String dataSource;
	final private String itemVar;
	private String indexVar;
	private Row startRow;
	private List<ForeachRowInfo> matchRows = LangUtils.newArrayList();
	private Row endRow;
	private String directiveEnd;
	
	public RowForeachDirectiveModel(String directive, String dataSource, String itemVar) {
		super();
		this.directiveStart = directive;
		this.dataSource = dataSource;
		this.itemVar = itemVar;
	}
	
	public int getLength(){
		return matchRows.size()+2;
	}
	public String getDirectiveStart() {
		return directiveStart;
	}

	public String getDataSource() {
		return dataSource;
	}
	public String getItemVar() {
		return itemVar;
	}
	public List<ForeachRowInfo> getMatchRows() {
		return matchRows;
	}
	public void addMatchRow(Row row){
		this.matchRows.add(new ForeachRowInfo(row));
	}
	public Row getEndRow() {
		return endRow;
	}
	public void setEndRow(Row endRow) {
		this.endRow = endRow;
	}
	public Row getStartRow() {
		return startRow;
	}
	public void setStartRow(Row startRow) {
		this.startRow = startRow;
	}

	public String getDirectiveEnd() {
		return directiveEnd;
	}

	public void setDirectiveEnd(String directiveEnd) {
		this.directiveEnd = directiveEnd;
	}
	
	public String getIndexVar() {
		return indexVar;
	}

	public void setIndexVar(String indexVar) {
		this.indexVar = indexVar;
	}



	public static class ForeachRowInfo {
		final private int originRownum;
		final private Row row;
		public ForeachRowInfo(Row row) {
			super();
			this.originRownum = row.getRowNum();
			this.row = row;
		}
		public int getOriginRownum() {
			return originRownum;
		}
		public Row getRow() {
			return row;
		}
		
	}
	
}
