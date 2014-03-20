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
		FieldModel field = cellContext.getFieldModel();
		int cellIndex = cellContext.getCellIndex();
		
		Object v = getFieldValue(cellContext.getObjectValue(), field, cellContext.getDefFieldValue());
		cellContext.setFieldValue(v);
		
		if(Collection.class.isInstance(v)){
			Collection<?> values = (Collection<?>) v;
			int rowCount = cellContext.getRowCount();
//			Row currentRow = null;
//			int cellIndex = row.getLastCellNum();
			for(Object value : values){
				cellContext.setFieldValue(value);
				this.doFieldValueExecutors(cellContext);
//				currentRow = row.getSheet().getRow(row.getRowNum()+rowCount);
				this.createSingleCell(cellContext, rowCount, cellIndex, value);
//				cellIndex = cell.getColumnIndex();
				rowCount++;
				cellContext.addRowSpanCount(1);
				
				//clear
				cellContext.setFieldValue(null);
			}
		}else{
//			this.createSingleCell(ele, row, field, cellIndex, v);
			super.processSingleField(cellContext);
		}
		

	}
	
	private Cell createSingleCell(CellContext cellContext, int rowCount, int cellIndex, Object cellValue){
		Cell cell = null;
		if(cellContext==null)
			throw new BaseException("the cell of row has not created yet : " + cellContext.getFieldModel().getName());

		CellContext subCellContext = createCellContext(this.generator.getExcelValueParser(), cellContext.getObjectValue(), rowCount, cellContext.getRowContext(), cellContext.getFieldModel(), cellIndex);
		cell = createCell(subCellContext);
		
		setCellValue(cellContext.getFieldModel(), cell, cellValue);
		
		return cell;
	}
	
	
}
