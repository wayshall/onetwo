package org.onetwo.ext.poi.excel.reader;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.ext.poi.excel.generator.CellValueConvertor;
import org.onetwo.ext.poi.utils.ExcelUtils;

public abstract class AbstractRowMapper<T> extends AbstractSSFRowMapperAdapter<T> {

	public AbstractRowMapper() {
		super();
	}


	public AbstractRowMapper(Map<String, CellValueConvertor> convertors) {
		super(convertors);
	}


	@Override
	public T mapDataRow(Sheet sheet, List<String> names, int rowIndex){
		Row row = sheet.getRow(rowIndex);
		return this.mapDataRow(names, row, rowIndex);
	}
	

	abstract public T mapDataRow(List<String> names, Row row, int rowIndex);
	

	protected int getTitleRowIndex() {
		return getDataRowStartIndex()-1;
	}
	
	@Override
	public List<String> mapTitleRow(Sheet sheet) {
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
	
}