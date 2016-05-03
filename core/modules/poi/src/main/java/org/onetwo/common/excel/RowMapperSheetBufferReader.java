package org.onetwo.common.excel;

import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.common.excel.exception.ExcelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class RowMapperSheetBufferReader<T> implements ExcelBufferReader<T> {
	
	private static final Logger logger = LoggerFactory.getLogger(RowMapperSheetBufferReader.class);
	
	private Sheet sheet;
	private int sheetIndex = 0;
	private List<String> names;
	private int rowCount = 0;
	private boolean initialized;
	private SSFRowMapper<T> mapper;
	
	private int currentRowNumber = 0;
	
	private boolean ignoreNullRow;
	

	public RowMapperSheetBufferReader(Sheet sheet, int sheetIndex, SSFRowMapper<T> mapper){
		this(sheet, sheetIndex, true, mapper);
	}
	
	public RowMapperSheetBufferReader(Sheet sheet, int sheetIndex, boolean ignoreNullRow, SSFRowMapper<T> mapper){
		this.sheet = sheet;
		this.sheetIndex = sheetIndex;
		this.mapper = mapper;
		this.ignoreNullRow = ignoreNullRow;
	}
	
	@Override
	public void initReader(){
		Assert.notNull(sheet);
		Assert.notNull(mapper);
		rowCount = sheet.getPhysicalNumberOfRows();
		if(rowCount>0){
			names = mapper.mapTitleRow(sheetIndex, sheet);
			currentRowNumber = mapper.getDataRowStartIndex();
		}
		logger.info("the sheet {} has total row number: {}", sheetIndex, rowCount);
		this.initialized = true;
	}

	@Override
	public T read(){
		if(!initialized)
			throw new ExcelException("buffer has not initialized!");
		
		if(!isEnd()) {
			Row row = sheet.getRow(currentRowNumber);
			T value = mapper.mapDataRow(sheet, names, row, currentRowNumber);
			currentRowNumber++;
			if(value==null && isIgnoreNullRow() && !isEnd()){
				logger.info("ignore null row {}", currentRowNumber);
				value = read();
			}
			return value;
		}else{
			return null;
		}
	}
	
	public boolean isEnd(){
		return currentRowNumber >= rowCount;
	}

	public int getDataRowStartIndex() {
		return mapper.getDataRowStartIndex();
	}

	public int getRowCount() {
		return rowCount;
	}

	public boolean isIgnoreNullRow() {
		return ignoreNullRow;
	}

	public void setIgnoreNullRow(boolean ignoreNullRow) {
		this.ignoreNullRow = ignoreNullRow;
	}

	public int getCurrentRowNumber() {
		return currentRowNumber;
	}
	
}
