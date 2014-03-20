package org.onetwo.common.excel;

import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.onetwo.common.interfaces.excel.ExcelValueParser;

public class WorkbookData {

	public final static WorkbookListener EMPTY_WORKBOOK_LISTENER = new EmptyWorkbookListener();
	private final Workbook workbook;
	private final ExcelValueParser excelValueParser;
	private final WorkbookListener workbookListener;
	private final WorkbookModel workbookModel;
//	private Map<ExecutorModel, FieldValueExecutor> fieldValueExecutors;

	public WorkbookData(WorkbookModel workbookModel, Workbook workbook, WorkbookListener workbookListener) {
		this(workbookModel, workbook, new DefaultExcelValueParser(null), workbookListener);
	}

	public WorkbookData(WorkbookModel workbookModel, Workbook workbook, Map<String, Object> context) {
		this(workbookModel, workbook, new DefaultExcelValueParser(context), EMPTY_WORKBOOK_LISTENER);
	}
	
	public WorkbookData(WorkbookModel workbookModel, Workbook workbook, ExcelValueParser excelValueParser) {
		this(workbookModel, workbook, excelValueParser, EMPTY_WORKBOOK_LISTENER);
	}

	public WorkbookData(WorkbookModel workbookModel, Workbook workbook, ExcelValueParser excelValueParser, WorkbookListener workbookListener) {
		super();
		this.workbook = workbook;
		this.excelValueParser = excelValueParser;
		this.workbookListener = workbookListener;
		this.workbookModel = workbookModel;
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

	public WorkbookModel getWorkbookModel() {
		return workbookModel;
	}

	/*public FieldValueExecutor getFieldValueExecutor(ExecutorModel model){
		return this.fieldValueExecutors.get(model);
	}
	
	public void addFieldValueExecutor(ExecutorModel model, FieldValueExecutor executor){
		if(fieldValueExecutors==null)
			fieldValueExecutors = Maps.newHashMap();
		fieldValueExecutors.put(model, executor);
	}*/

}
