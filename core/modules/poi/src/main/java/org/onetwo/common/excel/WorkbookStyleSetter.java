package org.onetwo.common.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public interface WorkbookStyleSetter {

	public void setStyle(Sheet row, int sheetIndex);
	public void setStyle(Row row, int rowIndex);
	public void setStyle(Cell cell, int cellIndex);

}
