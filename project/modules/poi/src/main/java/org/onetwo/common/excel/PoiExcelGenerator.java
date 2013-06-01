package org.onetwo.common.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.onetwo.common.interfaces.TemplateGenerator;
import org.onetwo.common.interfaces.excel.ExcelValueParser;

public interface PoiExcelGenerator extends TemplateGenerator {
	public ExcelValueParser getExcelValueParser();
	public void setExcelValueParser(ExcelValueParser excelValueParser);
	public HSSFWorkbook getWorkbook();

}