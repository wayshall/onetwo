package org.onetwo.common.excel;

import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.common.interfaces.excel.ExcelValueParser;

import com.google.common.collect.Maps;

public class SheetData {
	private final Sheet sheet;
	private final Object datasource;
	private final int sheetIndex;
	private final WorkbookData workbookData;
	private final TemplateModel sheetModel;;

	private Map<ExecutorModel, FieldValueExecutor> fieldValueExecutors;
	
	public SheetData(WorkbookData workbookData, TemplateModel sheetModel, Sheet sheet, Object dataSourceValue) {
		super();
		this.workbookData = workbookData;
		this.sheet = sheet;
		this.datasource = dataSourceValue;
		this.sheetIndex = workbookData.getWorkbook().getSheetIndex(sheet);
		this.sheetModel = sheetModel;
	}
	public Sheet getSheet() {
		return sheet;
	}
	public Object getDatasource() {
		return datasource;
	}
	public int getSheetIndex() {
		return sheetIndex;
	}
	public FieldValueExecutor getFieldValueExecutor(ExecutorModel model){
		return this.fieldValueExecutors.get(model);
	}
	
	public void addFieldValueExecutor(ExecutorModel model, FieldValueExecutor executor){
		if(fieldValueExecutors==null)
			fieldValueExecutors = Maps.newHashMap();
		fieldValueExecutors.put(model, executor);
	}
	public WorkbookData getWorkbookData() {
		return workbookData;
	}

	public ExcelValueParser getExcelValueParser() {
		return workbookData.getExcelValueParser();
	}
	public TemplateModel getSheetModel() {
		return sheetModel;
	}
	public String getLocation(){
		return this.sheetModel.getLabel();
	}
}
