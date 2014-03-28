package org.onetwo.common.excel.data;

import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.common.excel.FieldModel;

public class CellContextData extends AbstractExcelContextData {
	private final ExcelValueParser parser;
	private final Object objectValue;
	private final int rowCount;
	private final RowContextData rowContext;
	private final Row row;
	private final FieldModel fieldModel;
	private final Object defFieldValue;
	private Object fieldValue;
	private final int cellIndex;

	private int rowSpanCount = 0;
//	private Object fieldValue;
	
	public CellContextData(Object objectValue, int objectValueIndex, RowContextData rowContext, FieldModel field, int cellIndex, Object defValue) {
		super();
		this.rowContext = rowContext;
		this.row = rowContext.getCurrentRow();
		this.parser = rowContext.getExcelValueParser();
		this.objectValue = objectValue;
		if(objectValueIndex<1){
			objectValueIndex = row.getRowNum();
		}
		this.rowCount = objectValueIndex;
		this.fieldModel = field;
		//row.getLastCellNum()
		this.cellIndex = cellIndex;
		this.defFieldValue = defValue;
		
//		this.getSelfContext().clear();
//		this.getSelfContext().putAll(rowContext.getSelfContext());
	}
	protected AbstractExcelContextData getParentContextData(){
		return rowContext;
	}

	public void initData(){
		//由于cell context直接使用rowContext，屏蔽父类方法
	}
	

	@Override
	protected Object getRootObject() {
		return objectValue;
	}

	@Override
	public Map<String, Object> getSelfContext() {
//		return this.getWorkbookData().getCellContext();
		return this.getWorkbookData().getRowContext();
	}

	public WorkbookData getWorkbookData() {
		return rowContext.getWorkbookData();
	}

	public Row getCurrentRow(){
		Row currentRow = row.getSheet().getRow(rowCount);
		return currentRow;
	}
	
	/*public Row getCurrentRow(){
		if(rowCount==0)
			return row;
		int rowCount = rowCount + getRowSpan() -1;
		Row currentRow = row.getSheet().getRow(row.getRowNum()+rowCount);
		return currentRow;
	}*/
	
	public int getCellIndex(){
		/*int cellIndex = row.getSheet().getRow(row.getRowNum()+objectValueIndex).getLastCellNum();
		if(cellIndex==-1)
			cellIndex = 0;*/
		if(cellIndex==-1)
			return 0;
		return cellIndex;
	}
	
	public Sheet getSheet(){
		return row.getSheet();
	}
	
	public int getColSpan(){
		/*int colspan = 1;
		if(field.hasColspan()){
			colspan = parser.parseIntValue(field.getColspan(), objectValue);
		}
		if(colspan < 1) {
			colspan = 1;
		}*/
		return fieldModel.getColspanValue(this);
	}
	
	public int getRowSpan(){
		/*int rowspan = 1;
		if(field.hasRowspan()){
			rowspan = parser.parseIntValue(field.getRowspan(), objectValue);
		}
		if(rowspan < 1) {
			rowspan = 1;
		}*/
//		String name = field.getName()+"-rowspan";
//		UtilTimerStack.push(name);
		int rowspan = fieldModel.getRowpanValue(this);
//		UtilTimerStack.pop(name);
		return rowspan;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void addRowSpanCount(int rowSpanCount) {
		this.rowSpanCount += rowSpanCount;
	}

	public int getRowSpanCount() {
		return rowSpanCount;
	}

	public Object getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}

	public FieldModel getFieldModel() {
		return fieldModel;
	}

	/*public ExcelValueParser getParser() {
		return parser;
	}*/

	public Object getObjectValue() {
		return objectValue;
	}

	public Row getRow() {
		return row;
	}

	public Object getDefFieldValue() {
		return defFieldValue;
	}

	public RowContextData getRowContext() {
		return rowContext;
	}
	
	public String getLocation(){
		return rowContext.getLocation()+" -> " + fieldModel.getLabel();
	}

	@Override
	protected ExcelValueParser getExcelValueParser() {
		return parser;
	}
	
}
