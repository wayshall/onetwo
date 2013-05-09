package org.onetwo.common.excel;

import org.apache.poi.ss.usermodel.Sheet;

public interface ExcelDataExtractor<T> {

	public T extractData(Sheet sheet, int sheetIndex);

}