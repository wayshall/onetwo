package org.onetwo.ext.poi.excel.etemplate.directive;

import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.onetwo.ext.poi.excel.etemplate.AbstractDirectiveModel;

public class ForeachRowDirectiveModel extends AbstractDirectiveModel {

	final private String dataSource;
	final private String itemVar;
	private String indexVar;
	
	private List<?> dataList;
	
	public ForeachRowDirectiveModel(String directive, String dataSource, String itemVar) {
		super(directive);
		this.dataSource = dataSource;
		this.itemVar = itemVar;
	}
	
	public int getLength(){
		return getMatchRows().size()+2;
	}

	public String getDataSource() {
		return dataSource;
	}
	public String getItemVar() {
		return itemVar;
	}
	
	public String getIndexVar() {
		return indexVar;
	}

	public void setIndexVar(String indexVar) {
		this.indexVar = indexVar;
	}

	public List<?> getDataList() {
		return dataList;
	}

	public void setDataList(List<?> dataList) {
		this.dataList = dataList;
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
