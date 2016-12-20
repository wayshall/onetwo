package org.onetwo.ext.poi.excel.reader;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.onetwo.ext.poi.excel.exception.ExcelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class RowMapperWorkbookBufferReader<T> implements ExcelBufferReader<T> {
	
	private static final Logger logger = LoggerFactory.getLogger(RowMapperWorkbookBufferReader.class);
	
//	private Sheet sheet;
	private Workbook workbook;
	private int sheetCount = 0;
//	private List<String> names;

	/***
	 * 当前sheet的总行数
	 */
	private int rowCount = 0;
	private boolean initialized;
	private SheetRowMapper<T> mapper;

	private ExcelBufferReader<T> currentSheetReader;
	private int currentSheetIndex = 0;
//	private int currentRowCount = 0;
	
	/***
	 * 是否忽略null行（即返回null的行）
	 */
	private boolean ignoreNullRow;
	
	/*****
	 * 累计行数
	 */
	private int grandTotalRowNumber = 0;
	

	public RowMapperWorkbookBufferReader(Workbook workbook, SheetRowMapper<T> mapper){
		this(workbook, true, mapper);
	}
	public RowMapperWorkbookBufferReader(Workbook workbook, boolean ignoreNullRow, SheetRowMapper<T> mapper){
		this.workbook = workbook;
		this.mapper = mapper;
		this.ignoreNullRow = ignoreNullRow;
	}
	
	@Override
	public void initReader(){
		Assert.notNull(workbook);
		Assert.notNull(mapper);
//		rowCount = sheet.getPhysicalNumberOfRows();
		this.sheetCount = workbook.getNumberOfSheets();
		for (int i = 0; i < sheetCount; i++) {
			this.rowCount += workbook.getSheetAt(i).getPhysicalNumberOfRows();
		}
		if(rowCount>0){
			this.currentSheetReader = newReaderIfHasSheet();
		}
		logger.info("the workbook has total row number: {}", rowCount);
		this.initialized = true;
	}
	
	private ExcelBufferReader<T> newReaderIfHasSheet(){
		if(hasSheet()){
			Sheet sheet = workbook.getSheetAt(currentSheetIndex);
//			this.currentRowNumber = 0;
//			this.currentRowCount = sheet.getPhysicalNumberOfRows();
			ExcelBufferReader<T> reader = new RowMapperSheetBufferReader<T>(sheet, currentSheetIndex, isIgnoreNullRow(), mapper);
			reader.initReader();
			currentSheetIndex++;
			return reader;
		}else{
			return null;
		}
	}
	
	private boolean hasSheet(){
		return currentSheetIndex<sheetCount;
	}

	@Override
	public T read(){
		if(!initialized)
			throw new ExcelException("buffer has not initialized!");

		if(this.currentSheetReader==null)
			return null;
			
		if(this.currentSheetReader.isEnd()){
			//累计每张sheet的行数
			this.grandTotalRowNumber += this.currentSheetReader.getCurrentRowNumber();
			this.currentSheetReader = newReaderIfHasSheet();
			if(this.currentSheetReader==null)
				return null;
		}
		
		T obj = this.currentSheetReader.read();
//		this.currentRowNumber++;
		//如果有空行，继续读取下一条
		if(obj==null && !isEnd()){
			logger.info("ignore null row {}", getCurrentRowNumber());
			obj = read();
		}
		return obj;
	}

	public int getDataRowStartIndex() {
		return mapper.getDataRowStartIndex();
	}

	public int getRowCount() {
		return rowCount;
	}

	@Override
	public boolean isEnd() {
		return currentSheetIndex>=sheetCount && this.currentSheetReader.isEnd();
	}

	public int getCurrentRowNumber() {
		return grandTotalRowNumber + this.currentSheetReader.getCurrentRowNumber();
	}

	public boolean isIgnoreNullRow() {
		return ignoreNullRow;
	}

	public void setIgnoreNullRow(boolean ignoreNullRow) {
		this.ignoreNullRow = ignoreNullRow;
	}
	
}
