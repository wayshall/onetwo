package org.onetwo.common.excel;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.common.excel.utils.ExcelUtils;

public abstract class AbstractRowMapper<T> implements SSFRowMapper<T> {
	private Map<String, CellValueConvertor> convertors;

	public SSFRowMapper<T> register(String type, CellValueConvertor convertor){
		this.convertors.put(type, convertor);
		return this;
	}
	
	public SSFRowMapper<T> register(Map<String, CellValueConvertor> convertors){
		this.convertors.putAll(convertors);
		return this;
	}
	
	public AbstractRowMapper(){
		this(WorkbookReaderFactory.convertors);
	}
	
	public AbstractRowMapper(Map<String, CellValueConvertor> convertors){
		this.convertors = convertors;
	}
	

//	@Override
	public String getMapperName() {
		return this.getClass().getName();
	}

	@Override
	public T mapDataRow(Sheet sheet, List<String> names, Row row, int rowIndex){
		return this.mapDataRow(names, row, rowIndex);
	}
	

	abstract public T mapDataRow(List<String> names, Row row, int rowIndex);
	

	protected int getTitleRowIndex() {
		return getDataRowStartIndex()-1;
	}
	
	@Override
	public List<String> mapTitleRow(int sheetIndex, Sheet sheet){
		return this.mapTitleRow(sheet);
	}
	
	protected List<String> mapTitleRow(Sheet sheet) {
		try {
			Row titleRow = sheet.getRow(getTitleRowIndex());
			return ExcelUtils.getRowValues(titleRow);
		} catch (Exception e) {
			throw ExcelUtils.wrapAsUnCheckedException("mapTitleRow error" , e);
		}
	}

	@Override
	public int getDataRowStartIndex() {
		return 1;
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