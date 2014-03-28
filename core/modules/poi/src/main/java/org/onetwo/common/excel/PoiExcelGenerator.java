package org.onetwo.common.excel;

import org.apache.poi.ss.usermodel.Workbook;
import org.onetwo.common.excel.data.ExcelValueParser;
import org.onetwo.common.excel.data.WorkbookData;
import org.onetwo.common.interfaces.TemplateGenerator;

public interface PoiExcelGenerator extends TemplateGenerator {
	public PropertyStringParser getPropertyStringParser();
//	public ExcelValueParser getExcelValueParser();
//	public void setExcelValueParser(ExcelValueParser excelValueParser);
	public Workbook getWorkbook();
	public WorkbookData getWorkbookData();

}