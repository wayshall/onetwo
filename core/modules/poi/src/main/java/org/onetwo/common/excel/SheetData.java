package org.onetwo.common.excel;

import org.apache.poi.ss.usermodel.Sheet;

public class SheetData {
	private final Sheet sheet;
	private final Object datasource;
	private final int sheetIndex;
	
	public SheetData(WorkbookData workbookData, Sheet sheet, Object dataSourceValue) {
		super();
		this.sheet = sheet;
		this.datasource = dataSourceValue;
		this.sheetIndex = workbookData.getWorkbook().getSheetIndex(sheet);
	}
	public Sheet getSheet() {
		return sheet;
	}
	public Object getDatasource() {
		return datasource;
	}
	public int getSheetIndex() {
		return sheetIndex;
	}
	
}
