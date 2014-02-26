package org.onetwo.common.excel;

import java.util.Collection;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.onetwo.common.exception.BaseException;

public class SmartIteratorRowProcessor extends IteratorRowProcessor {

	public SmartIteratorRowProcessor(PoiExcelGenerator excelGenerator, RowProcessor titleRowProcessor) {
		super(excelGenerator, titleRowProcessor);
	}

	@Override
//	protected void processSingleField(Object ele, Row row, FieldModel field, Object defValue, int cellIndex){
	protected void processSingleField(CellContext cellContext){
//		Cell cell = createCell(row.getSheet(), row, field, -1, ele);
//		Object ele = cellContext.objectValue;
		Row row = cellContext.getCurrentRow();
		FieldModel field = cellContext.field;
		int cellIndex = cellContext.getCellIndex();
		
		Object v = getFieldValue(cellContext.objectValue, field, cellContext.defFieldValue);
		cellContext.setFieldValue(v);
		
		if(Collection.class.isInstance(v)){
			Collection<?> values = (Collection<?>) v;
			int rowCount = cellContext.getRowCount();
//			Row currentRow = null;
//			int cellIndex = row.getLastCellNum();
			for(Object value : values){
				this.doFieldValueExecutors(field, cellContext.parser, value);
//				currentRow = row.getSheet().getRow(row.getRowNum()+rowCount);
				this.createSingleCell(cellContext.objectValue, row, field, rowCount, cellIndex, value);
//				cellIndex = cell.getColumnIndex();
				rowCount++;
				cellContext.addRowSpanCount(1);
			}
		}else{
//			this.createSingleCell(ele, row, field, cellIndex, v);
			super.processSingleField(cellContext);
		}
		

	}
	
	private Cell createSingleCell(Object ele, Row row, FieldModel field, int rowCount, int cellIndex, Object cellValue){
		Cell cell = null;
		if(row==null)
			throw new BaseException("the cell of row has not created yet : " + field.getName());

		CellContext cellContext = createCellContext(this.generator.getExcelValueParser(), ele, rowCount, row, field, cellIndex);
		cell = createCell(cellContext);
		
		setCellValue(field, cell, cellValue);
		
		return cell;
	}
	
	
}
