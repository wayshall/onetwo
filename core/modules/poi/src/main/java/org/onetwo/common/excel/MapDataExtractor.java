package org.onetwo.common.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class MapDataExtractor<V extends ExtractorMapKey> implements ExcelDataExtractor<Map<String, V>>{

	private SSFRowMapper<V> mapper;
	
	public MapDataExtractor(SSFRowMapper<V> mapper) {
		super();
		this.mapper = mapper;
	}


	@Override
	public Map<String, V> extractData(Sheet sheet, int sheetIndex) {
		int rowCount = sheet.getPhysicalNumberOfRows();

		List<String> names = mapper.mapTitleRow(sheetIndex, sheet);

		Row row = null;
		Map<String, V> datas = new HashMap<String, V>();
		for (int rowIndex = mapper.getDataRowStartIndex(); rowIndex < rowCount; rowIndex++) {
			row = sheet.getRow(rowIndex);
			V value = mapper.mapDataRow(sheet, names, row, rowIndex);
			if (value == null)
				continue;
			datas.put(value.getExtractorMapKey(), value);
		}
		return datas;
	}

}
