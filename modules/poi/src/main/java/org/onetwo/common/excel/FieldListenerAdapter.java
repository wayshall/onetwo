package org.onetwo.common.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

@SuppressWarnings("serial")
public class FieldListenerAdapter implements FieldListener {
	
	/*public static Map<String, FieldListener> listeners = new HashMap<String, FieldListener>(){
		{
			put("test", new FieldListenerAdapter(){

				@Override
				public void afterCreateCell(Workbook workbook, Sheet sheet, Row row, Cell cell){
					CellStyle style = workbook.createCellStyle();
					cell.setCellStyle(style);
				}
				
			});
		}
	};*/
	
	@Override
	public Object getCellValue(Cell cell, Object value) {
		return formatValue(value);
	}
	
	public Object formatValue(Object value){
		return value;
	}
	
	@Override
	public void afterCreateCell(Workbook workbook, Sheet sheet, Row row, Cell cell) {
	}


}
