package org.onetwo.common.excel;

import org.apache.poi.ss.usermodel.Sheet;

public class SheetData {
	private final Sheet sheet;
	private final Object datasource;
	public SheetData(Sheet sheet, Object dataSourceValue) {
		super();
		this.sheet = sheet;
		this.datasource = dataSourceValue;
	}
	public Sheet getSheet() {
		return sheet;
	}
	public Object getDatasource() {
		return datasource;
	}
}
