package org.onetwo.common.excel.data;

import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.common.excel.ExecutorModel;
import org.onetwo.common.excel.FieldValueExecutor;
import org.onetwo.common.excel.TemplateModel;

import com.google.common.collect.Maps;

public class SheetData extends AbstractExcelContextData {
	private Sheet sheet;
	private Object datasource;
	private int sheetIndex;
	private final WorkbookData workbookData;
	private final TemplateModel sheetModel;
	private int totalSheet = 1;

	private Map<ExecutorModel, FieldValueExecutor> fieldValueExecutors;
	
	public SheetData(WorkbookData workbookData, TemplateModel sheetModel) {
		this.workbookData = workbookData;
//		this.sheet = sheet;
//		this.datasource = dataSourceValue;
		this.sheetModel = sheetModel;
	}

	public void setDatasource(Object datasource) {
		this.datasource = datasource;
	}

	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
		this.sheetIndex = workbookData.getWorkbook().getSheetIndex(sheet);
	}

	public void initData(){
		getSelfContext().clear();
		getSelfContext().putAll(getParentContextData().getSelfContext());
	}
	
	@Override
	public Map<String, Object> getSelfContext() {
		return this.workbookData.getSheetContext();
	}
	
	protected AbstractExcelContextData getParentContextData(){
		return workbookData;
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

	/*public ExcelValueParser getExcelValueParser() {
		return workbookData.getExcelValueParser();
	}*/
	public TemplateModel getSheetModel() {
		return sheetModel;
	}
	public String getLocation(){
		return this.sheetModel.getLabel();
	}
	@Override
	protected ExcelValueParser getExcelValueParser() {
		return workbookData.getExcelValueParser();
	}

	public int getTotalSheet() {
		return totalSheet;
	}

	public void setTotalSheet(int totalSheet) {
		this.totalSheet = totalSheet;
	}
	
	public boolean isLastSheet(){
		return (sheetIndex+1)==totalSheet;
	}
	
}
