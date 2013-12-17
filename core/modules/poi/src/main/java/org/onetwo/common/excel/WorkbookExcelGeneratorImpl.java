package org.onetwo.common.excel;

import org.apache.poi.ss.usermodel.Workbook;
import org.onetwo.common.interfaces.excel.ExcelValueParser;

public class WorkbookExcelGeneratorImpl extends AbstractWorkbookExcelGenerator {
	
	private WorkbookModel workbookModel;
	private Workbook workbook;

	@Override
	public void generateIt() {
		// TODO Auto-generated method stub
	}

	@Override
	public ExcelValueParser getExcelValueParser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setExcelValueParser(ExcelValueParser excelValueParser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Workbook getWorkbook() {
		return workbook;
	}

}
