package org.onetwo.ext.poi.utils;

import java.util.ArrayList;
import java.util.List;

public class ListExcelDataExtractor<T, TABLE> extends AbstractTableDataExtractor<T, List<T>, TABLE>{
	
	public ListExcelDataExtractor(TableRowMapper<T, TABLE> mapper) {
		super(mapper);
	}

	@Override
	protected List<T> createDataContainer(int size) {
		return new ArrayList<>(size);
	}

	@Override
	protected void addValueToContainer(List<T> container, T value) {
		container.add(value);
	}



}
