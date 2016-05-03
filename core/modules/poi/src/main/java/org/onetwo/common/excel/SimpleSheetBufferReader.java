package org.onetwo.common.excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.common.excel.exception.ExcelException;
import org.springframework.util.Assert;

public class SimpleSheetBufferReader implements ExcelBufferReader<Row> {
	
//	private static final Logger logger = MyLoggerFactory.getLogger(SheetBufferReader.class);
	
	private Sheet sheet;
	private int rowCount = 0;
	private boolean initialized;
	
	private int currentRowNumber = 0;
	
	public SimpleSheetBufferReader(Sheet sheet){
		this.sheet = sheet;
	}
	
	@Override
	public void initReader(){
		Assert.notNull(sheet);
		rowCount = sheet.getPhysicalNumberOfRows();
		this.initialized = true;
	}
	
	public boolean isEnd(){
		return currentRowNumber >= rowCount;
	}

	@Override
	public Row read(){
		if(!initialized)
			throw new ExcelException("buffer has not initialized!");
		
		if(!isEnd()) {
			Row row = sheet.getRow(currentRowNumber);
			currentRowNumber++;
			return row;
		}else{
			return null;
		}
	}

	public int getCurrentRowNumber() {
		return currentRowNumber;
	}
	
	
}
