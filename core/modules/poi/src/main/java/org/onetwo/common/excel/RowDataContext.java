package org.onetwo.common.excel;

import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.common.excel.DefaultRowProcessor.CellContext;

import com.google.common.collect.Maps;

public class RowDataContext {
	final private RowModel rowModel;
	final private SheetData sheetData;
	final private WorkbookData workbookData;
	private Row currentRow;
	private Map<String, CellContext> cellConextMap = Maps.newHashMap();
	
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
	
	public void putCellContext(String name, CellContext cellContext){
		this.cellConextMap.put(name, cellContext);
	}
	
	public CellContext getCellContext(String name){
		return this.cellConextMap.get(name);
	}
}
