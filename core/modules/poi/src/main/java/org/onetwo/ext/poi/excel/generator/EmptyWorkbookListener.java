package org.onetwo.ext.poi.excel.generator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class EmptyWorkbookListener implements WorkbookListener {

	@Override
	public void afterCreateWorkbook(Workbook wb) {
//		LangUtils.println("afterCreateWorkbook");
	}

	@Override
	public void afterCreateSheet(Sheet sheet, int sheetIndex) {
//		LangUtils.println("afterCreateSheet ${0} ", sheetIndex);
	}

	@Override
	public void afterCreateRow(Row row, int rowIndex) {
//		LangUtils.println("afterCreateRow ${0} ", rowIndex);
		
	}

	@Override
	public void afterCreateCell(Cell cell, int cellIndex) {
//		LangUtils.println("afterCreateCell ${0} ", cellIndex);
		
	}

}
