package org.onetwo.common.excel;

import org.apache.poi.hssf.usermodel.HSSFSheet;

public class RowDataContext {
	final private HSSFSheet sheet;
	final private RowModel rowModel;
	final private Object sheetDatas;
	
	public RowDataContext(Object sheetDatas, HSSFSheet sheet, RowModel rowModel) {
		super();
		this.sheet = sheet;
		this.rowModel = rowModel;
		this.sheetDatas = sheetDatas;
	}
	public HSSFSheet getSheet() {
		return sheet;
	}
	public RowModel getRowModel() {
		return rowModel;
	}
	public Object getSheetDatas() {
		return sheetDatas;
	}
	
	
}
