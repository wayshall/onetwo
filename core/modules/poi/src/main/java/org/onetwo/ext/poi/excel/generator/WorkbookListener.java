package org.onetwo.ext.poi.excel.generator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public interface WorkbookListener {

	public void afterCreateWorkbook(Workbook wb);
	public void afterCreateSheet(Sheet sheet, int sheetIndex);
	public void afterCreateRow(Row row, int rowIndex);
	public void afterCreateCell(Cell cell, int cellIndex);

}
