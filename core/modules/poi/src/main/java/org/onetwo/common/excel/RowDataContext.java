package org.onetwo.common.excel;

import org.apache.poi.ss.usermodel.Sheet;

public class RowDataContext {
	final private RowModel rowModel;
	final private SheetData sheetData;
	
	public RowDataContext(SheetData sheetData, RowModel rowModel) {
		super();
		this.rowModel = rowModel;
		this.sheetData = sheetData;
	}
	public Sheet getSheet() {
		return sheetData.getSheet();
	}
	public RowModel getRowModel() {
		return rowModel;
	}
	public Object getSheetData() {
		return sheetData;
	}
	
	
}
