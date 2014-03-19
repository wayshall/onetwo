package org.onetwo.common.excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class RowDataContext {
	final private RowModel rowModel;
	final private SheetData sheetData;
	final private WorkbookData workbookData;
	private Row currentRow;
	
	public RowDataContext(WorkbookData workbookData, SheetData sheetData, RowModel rowModel) {
		super();
		this.workbookData = workbookData;
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
	public WorkbookData getWorkbookData() {
		return workbookData;
	}
	public Row getCurrentRow() {
		return currentRow;
	}
	public void setCurrentRow(Row currentRow) {
		this.currentRow = currentRow;
	}
}
