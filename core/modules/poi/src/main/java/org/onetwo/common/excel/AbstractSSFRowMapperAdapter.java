package org.onetwo.common.excel;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.common.excel.utils.ExcelUtils;

public abstract class AbstractSSFRowMapperAdapter<T> implements SSFRowMapper<T> {
	private Map<String, CellValueConvertor> convertors;
	
	public AbstractSSFRowMapperAdapter(){
		this(WorkbookReaderFactory.convertors);
	}
	
	public AbstractSSFRowMapperAdapter(Map<String, CellValueConvertor> convertors){
		this.convertors = convertors;
	}
	
//	@Override
	public String getMapperName() {
		return this.getClass().getName();
	}

	public SSFRowMapper<T> register(String type, CellValueConvertor convertor){
		this.convertors.put(type, convertor);
		return this;
	}
	
	public SSFRowMapper<T> register(Map<String, CellValueConvertor> convertors){
		this.convertors.putAll(convertors);
		return this;
	}

	@Override
	public List<String> mapTitleRow(int sheetIndex, Sheet sheet){
		try {
			Row titleRow = sheet.getRow(0);
			return ExcelUtils.getRowValues(titleRow);
		} catch (Exception e) {
			throw ExcelUtils.wrapAsUnCheckedException("mapTitleRow error" , e);
		}
	}
	

	@Override
	public int getDataRowStartIndex() {
		return 1;
	}
	
	@SuppressWarnings({ "hiding", "unchecked" })
	protected <T> T getCellValue(Class<T> type, Row row, int cellnum){
		try {
			CellValueConvertor c = this.getCellValueConvertor(type);
			if(c==null){
				return (T)ExcelUtils.getCellValue(row.getCell(cellnum));
			}
			return (T)c.convert(row.getCell(cellnum));
		} catch (Exception e) {
			throw ExcelUtils.wrapAsUnCheckedException("获取excel列[row" +row.getRowNum()+", cell"+ cellnum+"]值出错：" + e.getMessage(), e);
		}
	}

	protected CellValueConvertor getCellValueConvertor(Class<?> type) {
		return getCellValueConvertor(type.getSimpleName());
	}

//	@Override
	public CellValueConvertor getCellValueConvertor(String type) {
		if(convertors==null || convertors.isEmpty())
			return null;
		if(ExcelUtils.isBlank(type))
			return null;
		return convertors.get(type.toLowerCase());
	}
	
};