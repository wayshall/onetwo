package org.onetwo.common.excel;

import org.apache.poi.hssf.usermodel.HSSFSheet;

public class SheetData {
	private final HSSFSheet sheet;
	private final Object datasource;
	public SheetData(HSSFSheet sheet, Object dataSourceValue) {
		super();
		this.sheet = sheet;
		this.datasource = dataSourceValue;
	}
	public HSSFSheet getSheet() {
		return sheet;
	}
	public Object getDatasource() {
		return datasource;
	}
}
