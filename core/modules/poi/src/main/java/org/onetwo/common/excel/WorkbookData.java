package org.onetwo.common.excel;

import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.onetwo.common.interfaces.excel.ExcelValueParser;

import com.google.common.collect.Maps;

public class WorkbookData {

	public final static WorkbookListener EMPTY_WORKBOOK_LISTENER = new EmptyWorkbookListener();
	private final Workbook workbook;
	private final ExcelValueParser excelValueParser;
	private final WorkbookListener workbookListener;
	private Map<ExecutorModel, FieldValueExecutor> fieldValueExecutors;

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
	
	public void addFieldValueExecutor(ExecutorModel model, FieldValueExecutor executor){
		if(fieldValueExecutors==null)
			fieldValueExecutors = Maps.newHashMap();
		fieldValueExecutors.put(model, executor);
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

	public FieldValueExecutor getFieldValueExecutor(ExecutorModel model){
		return this.fieldValueExecutors.get(model);
	}

}
