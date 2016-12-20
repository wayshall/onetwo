package org.onetwo.ext.poi.utils;

import java.util.List;

abstract public class AbstractTableDataExtractor<DATA, C, TABLE> implements TableDataExtractor<C, TABLE> {
	
	private TableRowMapper<DATA, TABLE> mapper;
	

	public AbstractTableDataExtractor(TableRowMapper<DATA, TABLE> mapper) {
		super();
		this.mapper = mapper;
	}

	@Override
	public C extractData(TABLE table) {
		int rowCount = mapper.getNumberOfRows(table);

		List<String> names = mapper.mapTitleRow(table);

		C datas = createDataContainer(rowCount);
		for (int rowIndex = mapper.getDataRowStartIndex(); rowIndex < rowCount; rowIndex++) {
			DATA value = mapper.mapDataRow(table, names, rowIndex);
			if (value == null)
				continue;
//			datas.add(value);
			addValueToContainer(datas, value);
		}
		return datas;
	}

	abstract protected C createDataContainer(int rowCount);
	abstract protected void addValueToContainer(C container, DATA value);

}
