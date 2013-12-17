package org.onetwo.common.excel;

import org.apache.poi.ss.usermodel.Workbook;
import org.onetwo.common.interfaces.TemplateGenerator;
import org.onetwo.common.interfaces.excel.ExcelValueParser;

public interface PoiExcelGenerator extends TemplateGenerator {
	public ExcelValueParser getExcelValueParser();
	public void setExcelValueParser(ExcelValueParser excelValueParser);
	public Workbook getWorkbook();

}