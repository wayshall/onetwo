package org.onetwo.ext.poi.excel.reader;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.common.exception.BaseException;
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
		if (row==null) {
			logger.info("row[{}] is null, ignore...", rowIndex);
			return null;
		}
		
		BidiMap<String, Integer> titleMap = toTitleMap(names);
		if (isIgnoreRow(titleMap, row)) {
			return null;
		}
		
		return this.mapDataRow(titleMap, names, row, rowIndex);
	}
	
	final protected BidiMap<String, Integer> toTitleMap(List<String> names) {
		BidiMap<String, Integer> titleMap = new TreeBidiMap<String, Integer>();
		int index = 0;
		for (String name : names) {
			titleMap.put(name, index);
			index++;
		}
		return titleMap;
	}
	
	protected boolean isIgnoreRow(BidiMap<String, Integer> titleMap, Row row) {
		Cell cell = row.getCell(0);
		return cell==null;
	}
	

	public T mapDataRow(BidiMap<String, Integer> titleMap, List<String> titleNames, Row row, int rowIndex) {
		return this.mapDataRow(titleNames, row, rowIndex);
	}
	
	abstract public T mapDataRow(List<String> names, Row row, int rowIndex);
	

	protected int getTitleRowIndex() {
		return getDataRowStartIndex()-1;
	}
	
	@Override
	public List<String> mapTitleRow(Sheet sheet) {
		int titleRowIndex = getTitleRowIndex();
		Row titleRow = sheet.getRow(titleRowIndex);
		if (titleRow==null) {
			throw new BaseException("title row can not be null. index: " + titleRowIndex);
		}
		try {
			return ExcelUtils.getRowValues(titleRow);
		} catch (Exception e) {
			throw ExcelUtils.wrapAsUnCheckedException("mapTitleRow error" , e);
		}
	}

	/****
	 * 默认数据行开始行为第二行
	 */
	@Override
	public int getDataRowStartIndex() {
		return 1;
	}
	
}