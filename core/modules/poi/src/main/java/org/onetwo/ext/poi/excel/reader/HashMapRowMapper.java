package org.onetwo.ext.poi.excel.reader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.ext.poi.utils.ExcelUtils;

public class HashMapRowMapper extends AbstractRowMapper<Map<String, Object>> {
	/***
	 * index from 0...
	 */
	private int dataRowStartIndex;
	private int titleRowIndex;

	public HashMapRowMapper() {
		this(0, 1);
	}
	
	public HashMapRowMapper(int titleRowIndex, int dataRowStartIndex) {
		super(WorkbookReaderFactory.convertors);
		this.dataRowStartIndex = dataRowStartIndex;
		this.titleRowIndex = titleRowIndex;
	}

	protected int getTitleRowIndex() {
		return titleRowIndex;
	}
	
	@Override
	public List<String> mapTitleRow(Sheet sheet) {
		List<String> list = super.mapTitleRow(sheet);
		return list;
	}

	@Override
	public int getDataRowStartIndex() {
		return dataRowStartIndex;
	}

	@Override
	public Map<String, Object> mapDataRow(List<String> names, Row row, int rowIndex) {
//		int cellCount = row.getPhysicalNumberOfCells();

		Map<String, Object> rowMap = new HashMap<>();
		String key = null;
		Object val = null;
		for (int i = 0; i < names.size(); i++) {
			Cell cell = row.getCell(i);
			if(cell==null)
				continue;
			key = names.get(i);
			val = getCellValue(cell);
			rowMap.put(key, val);
		}
		return rowMap;
	}
	
	protected Object getCellValue(Cell cell){
		return ExcelUtils.getCellValue(cell);
	}


}
