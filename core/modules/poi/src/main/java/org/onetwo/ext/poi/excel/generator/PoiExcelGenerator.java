package org.onetwo.ext.poi.excel.generator;

import org.apache.poi.ss.usermodel.Workbook;
import org.onetwo.ext.poi.excel.data.WorkbookData;
import org.onetwo.ext.poi.excel.interfaces.TemplateGenerator;

public interface PoiExcelGenerator extends TemplateGenerator {
	public PropertyStringParser getPropertyStringParser();
//	public ExcelValueParser getExcelValueParser();
//	public void setExcelValueParser(ExcelValueParser excelValueParser);
	public Workbook getWorkbook();
	public WorkbookData getWorkbookData();

}