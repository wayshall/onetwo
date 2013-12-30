package org.onetwo.common.excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;

public class DefaultSheetBufferReader implements SheetBufferReader<Row> {
	
//	private static final Logger logger = MyLoggerFactory.getLogger(SheetBufferReader.class);
	
	private Sheet sheet;
	private int rowCount = 0;
	private boolean initialized;
	
	private int currentRowNumber;
	
	public DefaultSheetBufferReader(Sheet sheet){
		this.sheet = sheet;
	}
	
	@Override
	public void initReader(){
		Assert.notNull(sheet);
		rowCount = sheet.getPhysicalNumberOfRows();
		if(rowCount<1)
			return ;
		currentRowNumber = 0;
		this.initialized = true;
	}

	@Override
	public Row read(){
		if(!initialized)
			throw new BaseException("buffer has not initialized!");
		
		if(currentRowNumber < rowCount) {
			Row row = sheet.getRow(currentRowNumber);
			currentRowNumber++;
			return row;
		}else{
			return null;
		}
	}
}
