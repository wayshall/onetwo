package org.onetwo.common.excel.data;

import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.common.excel.RowModel;

import com.google.common.collect.Maps;

public class RowContextData extends AbstractExcelContextData {
	final private RowModel rowModel;
	final private SheetData sheetData;
	private Row currentRow;
	private Object currentRowObject;
	private Map<String, CellContextData> childConextDatas = Maps.newHashMap();
	
	public RowContextData(SheetData sheetData, RowModel rowModel) {
		super();
		this.rowModel = rowModel;
		this.sheetData = sheetData;
	}

	public void initData(){
		getSelfContext().clear();
		getSelfContext().putAll(getParentContextData().getSelfContext());
	}

	protected Object getRootObject() {
		return currentRowObject;
//		return null;
	}
	
	@Override
	public Map<String, Object> getSelfContext() {
		return this.getWorkbookData().getRowContext();
	}
	protected AbstractExcelContextData getParentContextData(){
		return sheetData;
	}

	public WorkbookData getWorkbookData() {
		return sheetData.getWorkbookData();
	}
	public Sheet getSheet() {
		return sheetData.getSheet();
	}
	public RowModel getRowModel() {
		return rowModel;
	}
	public SheetData getSheetData() {
		return sheetData;
	}
	/*public WorkbookData getWorkbookData() {
		return workbookData;
	}*/
	public Row getCurrentRow() {
		return currentRow;
	}
	public void setCurrentRow(Row currentRow) {
		this.currentRow = currentRow;
	}
	
	public void putChildCellContextData(String name, CellContextData cellContext){
		this.childConextDatas.put(name, cellContext);
	}
	
	public CellContextData getCellContext(String name){
		return this.childConextDatas.get(name);
	}
	public String getLocation(){
		return sheetData.getLocation()+" -> " + rowModel.getName();
	}
	@Override
	protected ExcelValueParser getExcelValueParser() {
		return sheetData.getWorkbookData().getExcelValueParser();
	}

	public Object getCurrentRowObject() {
		return currentRowObject;
	}

	public void setCurrentRowObject(Object currentRowObject) {
		this.currentRowObject = currentRowObject;
	}

}
