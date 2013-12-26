package org.onetwo.common.excel;

import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.onetwo.common.interfaces.excel.ExcelValueParser;

public class WorkbookData {

	public final static WorkbookListener EMPTY_WORKBOOK_LISTENER = new EmptyWorkbookListener();
	private final Workbook workbook;
	private final ExcelValueParser excelValueParser;
	private final WorkbookListener workbookListener;

	public WorkbookData(Workbook workbook, WorkbookListener workbookListener) {
		this(workbook, new DefaultExcelValueParser(null), workbookListener);
	}

	public WorkbookData(Workbook workbook, Map<String, Object> context) {
		this(workbook, new DefaultExcelValueParser(context), EMPTY_WORKBOOK_LISTENER);
	}
	

	public WorkbookData(Workbook workbook, ExcelValueParser excelValueParser) {
		this(workbook, excelValueParser, EMPTY_WORKBOOK_LISTENER);
	}

	public WorkbookData(Workbook workbook, ExcelValueParser excelValueParser, WorkbookListener workbookListener) {
		super();
		this.workbook = workbook;
		this.excelValueParser = excelValueParser;
		this.workbookListener = workbookListener;
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	public ExcelValueParser getExcelValueParser() {
		return excelValueParser;
	}

	public WorkbookListener getWorkbookListener() {
		return workbookListener;
	}


}
