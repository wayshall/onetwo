package org.onetwo.common.excel;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ListExcelDataExtractorImpl<T> implements ExcelDataExtractor<List<T>> {
	
	private SSFRowMapper<T> mapper;
	
	

	public ListExcelDataExtractorImpl(SSFRowMapper<T> mapper) {
		super();
		this.mapper = mapper;
	}



	@Override
	public List<T> extractData(Sheet sheet, int sheetIndex) {
		int rowCount = sheet.getPhysicalNumberOfRows();

		List<String> names = mapper.mapTitleRow(sheetIndex, sheet);

		Row row = null;
		List<T> datas = new ArrayList<T>();
		for (int rowIndex = mapper.getDataRowStartIndex(); rowIndex < rowCount; rowIndex++) {
			row = sheet.getRow(rowIndex);
			T value = mapper.mapDataRow(sheet, names, row, rowIndex);
			if (value == null)
				continue;
			datas.add(value);
		}
		return datas;
	}

}
