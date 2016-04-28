package org.onetwo.common.excel.etemplate;

import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.onetwo.common.utils.LangUtils;

public class RowForeachDirectiveModel extends AbstractDirectiveModel {

	final private String dataSource;
	final private String itemVar;
	private String indexVar;
	private List<ForeachRowInfo> matchRows = LangUtils.newArrayList();
	
	public RowForeachDirectiveModel(String directive, String dataSource, String itemVar) {
		super(directive);
		this.dataSource = dataSource;
		this.itemVar = itemVar;
	}
	
	public int getLength(){
		return matchRows.size()+2;
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
