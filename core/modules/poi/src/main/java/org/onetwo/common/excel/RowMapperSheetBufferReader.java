package org.onetwo.common.excel;

import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;

public class RowMapperSheetBufferReader<T> implements SheetBufferReader<T> {
	
//	private static final Logger logger = MyLoggerFactory.getLogger(SheetBufferReader.class);
	
	private Sheet sheet;
	private int sheetIndex = 0;
	private List<String> names;
	private int rowCount = 0;
	private boolean initialized;
	private SSFRowMapper<T> mapper;
	
	private int currentRowNumber;
	
	public RowMapperSheetBufferReader(Sheet sheet, int sheetIndex, SSFRowMapper<T> mapper){
		this.sheet = sheet;
		this.sheetIndex = sheetIndex;
		this.mapper = mapper;
	}
	
	@Override
	public void initReader(){
		Assert.notNull(sheet);
		Assert.notNull(mapper);
		rowCount = sheet.getPhysicalNumberOfRows();
		if(rowCount<1)
			return ;
		names = mapper.mapTitleRow(sheetIndex, sheet);
		currentRowNumber = mapper.getDataRowStartIndex();
		this.initialized = true;
	}

	@Override
	public T read(){
		if(!initialized)
			throw new BaseException("buffer has not initialized!");
		
		if(currentRowNumber < rowCount) {
			Row row = sheet.getRow(currentRowNumber);
			T value = mapper.mapDataRow(sheet, names, row, currentRowNumber);
			currentRowNumber++;
			return value;
		}else{
			return null;
		}
	}

	public int getDataRowStartIndex() {
		return mapper.getDataRowStartIndex();
	}
}
