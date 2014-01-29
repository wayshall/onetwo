package org.onetwo.common.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;

public class RowMapperWorkbookBufferReader<T> implements ExcelBufferReader<T> {
	
//	private static final Logger logger = MyLoggerFactory.getLogger(SheetBufferReader.class);
	
//	private Sheet sheet;
	private Workbook workbook;
	private int sheetCount = 0;
//	private List<String> names;
	private int rowCount = 0;
	private boolean initialized;
	private SSFRowMapper<T> mapper;

	private ExcelBufferReader<T> currentSheetReader;
	private int currentSheetIndex = 0;
	private int currentRowCount = 0;
	private int currentRowNumber = 0;
	
	public RowMapperWorkbookBufferReader(Workbook workbook, SSFRowMapper<T> mapper){
		this.workbook = workbook;
		this.mapper = mapper;
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
		this.initialized = true;
	}
	
	private ExcelBufferReader<T> newReaderIfHasSheet(){
		if(hasSheet()){
			Sheet sheet = workbook.getSheetAt(currentSheetIndex);
			this.currentRowNumber = 0;
			this.currentRowCount = sheet.getPhysicalNumberOfRows();
			ExcelBufferReader<T> reader = new RowMapperSheetBufferReader<T>(sheet, currentSheetIndex, mapper);
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
			throw new BaseException("buffer has not initialized!");

		if(this.currentSheetReader==null)
			return null;
			
		if(this.currentSheetReader.isEnd()){
			this.currentSheetReader = newReaderIfHasSheet();
			if(this.currentSheetReader==null)
				return null;
		}
		
		T obj = this.currentSheetReader.read();
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
		return currentSheetIndex>=sheetCount && currentRowNumber>=currentRowCount;
	}
	
}
