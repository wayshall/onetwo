package org.onetwo.ext.poi.excel.generator;

import java.util.Collection;

import org.apache.poi.ss.usermodel.Cell;
import org.onetwo.ext.poi.excel.data.CellContextData;
import org.onetwo.ext.poi.excel.exception.ExcelException;

public class SmartIteratorRowProcessor extends IteratorRowProcessor {

	public SmartIteratorRowProcessor(PoiExcelGenerator excelGenerator, RowProcessor titleRowProcessor) {
		super(excelGenerator, titleRowProcessor);
	}

	@Override
	protected void processSingleField(CellContextData cellContext){
		int cellIndex = cellContext.getCellIndex();
		
		Object v = getFieldValue(cellContext);
		cellContext.setFieldValue(v);
		
		if(Collection.class.isInstance(v)){
			Collection<?> values = (Collection<?>) v;
			int rowCount = cellContext.getRowCount();
			for(Object value : values){
				cellContext.setFieldValue(value);
				this.doFieldValueExecutors(cellContext);
				this.createSingleCell(cellContext, rowCount, cellIndex, value);
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
	
	private Cell createSingleCell(CellContextData cellContext, int rowCount, int cellIndex, Object cellValue){
		Cell cell = null;
		if(cellContext==null)
			throw new ExcelException("the cell of row has not created yet : " + cellContext.getFieldModel().getName());

		CellContextData subCellContext = createCellContext(cellContext.getObjectValue(), rowCount, cellContext.getRowContext(), cellContext.getFieldModel(), cellIndex);
		cell = createCell(subCellContext);
		
		setCellValue(cellContext.getFieldModel(), cell, cellValue);
		
		return cell;
	}
	
	
}
