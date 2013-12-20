package org.onetwo.common.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class SheetData {
	private final Sheet sheet;
	private final Object datasource;
	private final int sheetIndex;
	
	public SheetData(Workbook workbook, Sheet sheet, Object dataSourceValue) {
		super();
		this.sheet = sheet;
		this.datasource = dataSourceValue;
		this.sheetIndex = workbook.getSheetIndex(sheet);
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
