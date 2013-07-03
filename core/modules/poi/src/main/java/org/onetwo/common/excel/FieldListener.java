package org.onetwo.common.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public interface FieldListener {
	
	public void afterCreateCell(Workbook workbook, Sheet sheet, Row row, Cell cell);
	
	public Object getCellValue(Cell cell, Object value);
}
